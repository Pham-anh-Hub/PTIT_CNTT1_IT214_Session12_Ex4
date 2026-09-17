package microservice.recommendationservice.service.impl;

import microservice.recommendationservice.dto.RecommendationProductDto;
import microservice.recommendationservice.model.RecommendationProduct;
import microservice.recommendationservice.repository.RecommendationRepository;
import microservice.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public List<RecommendationProductDto> getRecommendationsForProduct(Long productId) {
        List<RecommendationProduct> list = recommendationRepository.findByTargetProductId(productId);
        if (list.isEmpty()) {
            // Nếu không tìm thấy theo targetProductId cụ thể, trả về danh sách gợi ý chung mặc định
            list = recommendationRepository.findAll();
        }
        return list.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecommendationProductDto> getAllRecommendations() {
        return recommendationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RecommendationProductDto mapToDto(RecommendationProduct item) {
        return RecommendationProductDto.builder()
                .id(item.getId())
                .productName(item.getProductName())
                .category(item.getCategory())
                .price(item.getPrice())
                .targetProductId(item.getTargetProductId())
                .discountRate(item.getDiscountRate())
                .build();
    }
}
