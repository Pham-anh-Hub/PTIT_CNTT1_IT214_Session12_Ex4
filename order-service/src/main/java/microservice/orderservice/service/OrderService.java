package microservice.orderservice.service;

import microservice.orderservice.dto.CheckoutResponseDto;
import microservice.orderservice.dto.OrderRequest;
import microservice.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
    OrderResponse updateOrder(Long id, OrderRequest request);
    void deleteOrder(Long id);
    CheckoutResponseDto getCheckoutInformation(Long orderId);
    microservice.orderservice.dto.OrderCheckoutResponse checkout(OrderRequest request);
}
