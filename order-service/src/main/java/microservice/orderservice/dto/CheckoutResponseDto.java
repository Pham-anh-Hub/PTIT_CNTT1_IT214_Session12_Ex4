package microservice.orderservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDto {
    private OrderResponse order;
    private List<RecommendationProductDto> recommendedProducts;
    private LocalDateTime checkoutTime;
    private String checkoutNote;
}
