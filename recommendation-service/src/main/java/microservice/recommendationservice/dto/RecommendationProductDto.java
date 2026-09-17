package microservice.recommendationservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationProductDto {
    private Long id;
    private String productName;
    private String category;
    private Double price;
    private Long targetProductId;
    private Integer discountRate;
}
