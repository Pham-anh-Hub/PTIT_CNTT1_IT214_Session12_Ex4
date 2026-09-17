package microservice.orderservice.service;

import microservice.orderservice.dto.RecommendationProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationClientService {

    private final RestTemplate restTemplate;

    @Value("${services.recommendation-service.url:http://localhost:8082}")
    private String recommendationServiceUrl;

    public List<RecommendationProductDto> getRecommendedProductsForProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return getRecommendedProducts((Long) null);
        }

        Map<Long, RecommendationProductDto> combinedResults = new LinkedHashMap<>();

        for (Long productId : productIds) {
            List<RecommendationProductDto> list = getRecommendedProducts(productId);
            for (RecommendationProductDto dto : list) {
                combinedResults.putIfAbsent(dto.getId(), dto);
            }
        }

        return new ArrayList<>(combinedResults.values());
    }

    public List<RecommendationProductDto> getRecommendedProducts(Long productId) {
        try {
            String url = recommendationServiceUrl + "/api/recommendations/products?productId=" + (productId != null ? productId : "");
            log.info("[RestTemplate Call] Calling Recommendation-Service: GET {}", url);

            ResponseEntity<List<RecommendationProductDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecommendationProductDto>>() {}
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            log.error("[RestTemplate Error] Error calling recommendation-service: {}", e.getMessage());
        }
        return Collections.emptyList();
    }
}
