package microservice.recommendationservice.controller;

import microservice.recommendationservice.dto.RecommendationProductDto;
import microservice.recommendationservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/bought-together")
    public ResponseEntity<List<RecommendationProductDto>> getBoughtTogetherProducts(
            @RequestParam(name = "productId", required = false) Long productId) {
        if (productId != null) {
            return ResponseEntity.ok(recommendationService.getRecommendationsForProduct(productId));
        }
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    @GetMapping("/products")
    public ResponseEntity<List<RecommendationProductDto>> getRecommendationProducts(
            @RequestParam(name = "productId", required = false) Long productId) {
        if (productId != null) {
            return ResponseEntity.ok(recommendationService.getRecommendationsForProduct(productId));
        }
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<RecommendationProductDto>> suggestProducts(
            @RequestParam(name = "userId", required = false) Long userId) {
        return ResponseEntity.ok(recommendationService.getAllRecommendations());
    }
}
