package microservice.recommendationservice;

import microservice.recommendationservice.model.RecommendationProduct;
import microservice.recommendationservice.repository.RecommendationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@SpringBootApplication
@EnableAsync
public class RecommendationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(RecommendationRepository repository) {
        return args -> {
            repository.saveAll(List.of(
                    RecommendationProduct.builder()
                            .productName("Ốp lưng MagSafe iPhone")
                            .category("Phụ kiện")
                            .price(250000.0)
                            .targetProductId(101L)
                            .discountRate(15)
                            .build(),
                    RecommendationProduct.builder()
                            .productName("Kính cường lực Kingkong")
                            .category("Phụ kiện")
                            .price(120000.0)
                            .targetProductId(101L)
                            .discountRate(20)
                            .build(),
                    RecommendationProduct.builder()
                            .productName("Sạc nhanh Anker 20W")
                            .category("Củ sạc")
                            .price(350000.0)
                            .targetProductId(101L)
                            .discountRate(10)
                            .build(),
                    RecommendationProduct.builder()
                            .productName("Chuột không dây Logitech MX Master 3S")
                            .category("Chuột máy tính")
                            .price(1850000.0)
                            .targetProductId(102L)
                            .discountRate(10)
                            .build(),
                    RecommendationProduct.builder()
                            .productName("Túi chống sốc Tomtoc")
                            .category("Túi xách")
                            .price(450000.0)
                            .targetProductId(102L)
                            .discountRate(15)
                            .build(),
                    RecommendationProduct.builder()
                            .productName("Hub Chuyển Đổi Type-C 7 in 1")
                            .category("Cáp chuyển đổi")
                            .price(650000.0)
                            .targetProductId(102L)
                            .discountRate(12)
                            .build()
            ));
        };
    }
}
