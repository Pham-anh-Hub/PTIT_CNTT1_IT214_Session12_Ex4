package microservice.orderservice.service.impl;

import microservice.orderservice.dto.*;
import microservice.orderservice.event.OrderCreatedEvent;
import microservice.orderservice.event.OrderEventPublisher;
import microservice.orderservice.model.Order;
import microservice.orderservice.model.OrderItem;
import microservice.orderservice.repository.OrderRepository;
import microservice.orderservice.service.OrderService;
import microservice.orderservice.service.RecommendationClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RecommendationClientService recommendationClientService;
    private final OrderEventPublisher orderEventPublisher;
    private final org.springframework.web.client.RestTemplate restTemplate;

    @Override
    public OrderCheckoutResponse checkout(OrderRequest request) {
        // Luồng xử lý đơn hàng chính
        if (request.getCustomerName() != null) {
            createOrder(request);
        }

        // Gọi sang Recommendation-Service với cơ chế Timeout & Fallback (try-catch)
        String recommendUrl = "http://localhost:8082/api/recommendations/suggest?userId=" + (request.getUserId() != null ? request.getUserId() : 1);
        List suggestions;

        try {
            suggestions = restTemplate.getForObject(recommendUrl, List.class);
            if (suggestions == null) {
                suggestions = Collections.emptyList();
            }
        } catch (Exception e) {
            // Fallback: Quá 3 giây không lấy được gợi ý (hoặc gặp sự cố) -> Trả về danh sách rỗng []
            log.warn("[Fallback] Không lấy được gợi ý sản phẩm từ Recommendation-Service (Timeout/Error): {}. Tự động fallback về danh sách rỗng [].", e.getMessage());
            suggestions = Collections.emptyList();
        }

        // Vẫn đảm bảo khách thanh toán thành công
        return new OrderCheckoutResponse("Thành công", suggestions);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setStatus(request.getStatus() != null && !request.getStatus().isBlank() ? request.getStatus() : "PENDING");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> itemList = new ArrayList<>();

        // 1. Trường hợp người dùng truyền danh sách items: List<OrderItemRequest>
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (OrderItemRequest itemReq : request.getItems()) {
                OrderItem item = OrderItem.builder()
                        .productId(itemReq.getProductId())
                        .productName(itemReq.getProductName())
                        .quantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : 1)
                        .unitPrice(itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : 0.0)
                        .build();
                itemList.add(item);
            }
        } 
        // 2. Trường hợp người dùng truyền 1 sản phẩm đơn lẻ ngắn gọn
        else if (request.getProductId() != null) {
            OrderItem item = OrderItem.builder()
                    .productId(request.getProductId())
                    .productName(request.getProductName() != null ? request.getProductName() : "Product #" + request.getProductId())
                    .quantity(request.getQuantity() != null ? request.getQuantity() : 1)
                    .unitPrice(request.getUnitPrice() != null ? request.getUnitPrice() : 0.0)
                    .build();
            itemList.add(item);
        }

        order.setItems(itemList);

        // Tự động tính tổng tiền (totalAmount)
        if (request.getTotalAmount() != null && request.getTotalAmount() > 0 && itemList.isEmpty()) {
            order.setTotalAmount(request.getTotalAmount());
        } else {
            order.calculateTotalAmount();
        }

        Order savedOrder = orderRepository.save(order);

        // Bắn sự kiện bất đồng bộ OrderCreatedEvent
        List<Long> productIds = savedOrder.getItems().stream()
                .map(OrderItem::getProductId)
                .filter(id -> id != null)
                .collect(Collectors.toList());

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .customerName(savedOrder.getCustomerName())
                .productIds(productIds)
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .build();
        orderEventPublisher.publishOrderCreatedEvent(event);

        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (request.getCustomerName() != null) order.setCustomerName(request.getCustomerName());
        if (request.getCustomerEmail() != null) order.setCustomerEmail(request.getCustomerEmail());
        if (request.getStatus() != null) order.setStatus(request.getStatus());

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            order.getItems().clear();
            for (OrderItemRequest itemReq : request.getItems()) {
                OrderItem item = OrderItem.builder()
                        .productId(itemReq.getProductId())
                        .productName(itemReq.getProductName())
                        .quantity(itemReq.getQuantity() != null ? itemReq.getQuantity() : 1)
                        .unitPrice(itemReq.getUnitPrice() != null ? itemReq.getUnitPrice() : 0.0)
                        .build();
                order.getItems().add(item);
            }
            order.calculateTotalAmount();
        } else if (request.getTotalAmount() != null) {
            order.setTotalAmount(request.getTotalAmount());
        }

        Order updatedOrder = orderRepository.save(order);
        return mapToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CheckoutResponseDto getCheckoutInformation(Long orderId) {
        OrderResponse orderResponse = getOrderById(orderId);

        List<Long> productIds = orderResponse.getItems() != null ?
                orderResponse.getItems().stream().map(OrderItemResponse::getProductId).filter(id -> id != null).collect(Collectors.toList()) :
                List.of();

        // Gọi sang Recommendation-Service bằng RestTemplate để lấy danh sách "Sản phẩm mua kèm"
        List<RecommendationProductDto> recommendedProducts = recommendationClientService.getRecommendedProductsForProducts(productIds);

        return CheckoutResponseDto.builder()
                .order(orderResponse)
                .recommendedProducts(recommendedProducts)
                .checkoutTime(LocalDateTime.now())
                .checkoutNote("Vui lòng kiểm tra lại đơn hàng và danh sách sản phẩm mua kèm ưu đãi trước khi xác nhận thanh toán.")
                .build();
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems() != null ?
                order.getItems().stream().map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subTotal((item.getUnitPrice() != null ? item.getUnitPrice() : 0.0) * (item.getQuantity() != null ? item.getQuantity() : 1))
                        .build()).collect(Collectors.toList()) :
                List.of();

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .items(itemResponses)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
