package microservice.recommendationservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recommendation_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendationProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String category;
    private Double price;
    private Long targetProductId; // ID của sản phẩm chính để gợi ý mua kèm
    private Integer discountRate; // Tỷ lệ giảm giá khi mua kèm (VD: 10 = 10%)
}
