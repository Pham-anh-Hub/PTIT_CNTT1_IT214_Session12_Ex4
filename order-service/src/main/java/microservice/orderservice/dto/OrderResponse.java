package microservice.orderservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String customerName;
    private String customerEmail;
    private List<OrderItemResponse> items;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
}
