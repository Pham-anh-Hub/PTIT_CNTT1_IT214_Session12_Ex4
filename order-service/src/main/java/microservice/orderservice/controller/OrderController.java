package microservice.orderservice.controller;

import microservice.orderservice.dto.CheckoutResponseDto;
import microservice.orderservice.dto.OrderRequest;
import microservice.orderservice.dto.OrderResponse;
import microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse createdOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable("id") Long id, @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/checkout")
    public ResponseEntity<CheckoutResponseDto> getCheckoutInformation(@PathVariable("id") Long id) {
        CheckoutResponseDto checkoutInfo = orderService.getCheckoutInformation(id);
        return ResponseEntity.ok(checkoutInfo);
    }

    @PostMapping("/checkout")
    public ResponseEntity<microservice.orderservice.dto.OrderCheckoutResponse> checkout(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.checkout(request));
    }
}
