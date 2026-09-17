package microservice.orderservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
}
