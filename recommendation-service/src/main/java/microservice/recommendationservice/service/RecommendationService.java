package microservice.recommendationservice.service;

import microservice.recommendationservice.dto.RecommendationProductDto;

import java.util.List;

public interface RecommendationService {
    List<RecommendationProductDto> getRecommendationsForProduct(Long productId);
    List<RecommendationProductDto> getAllRecommendations();
}
