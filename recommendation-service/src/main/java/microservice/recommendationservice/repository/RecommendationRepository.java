package microservice.recommendationservice.repository;

import microservice.recommendationservice.model.RecommendationProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<RecommendationProduct, Long> {
    List<RecommendationProduct> findByTargetProductId(Long targetProductId);
}
