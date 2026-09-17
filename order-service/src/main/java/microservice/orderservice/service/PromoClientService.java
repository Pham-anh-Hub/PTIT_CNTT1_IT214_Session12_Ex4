package microservice.orderservice.service;

import microservice.orderservice.exception.VoucherNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromoClientService {

    private final RestTemplate restTemplate;

    @Value("${services.promo-service.url:http://localhost:8083}")
    private String promoServiceUrl;

    @CircuitBreaker(name = "promoClient", fallbackMethod = "promoFallback")
    public Double validateVoucher(String voucherCode) {
        if (voucherCode == null || voucherCode.isBlank()) {
            return 0.0;
        }

        try {
            String url = promoServiceUrl + "/api/promos/validate?code=" + voucherCode;
            log.info("[Promo-Service Call] Validating voucher: {}", voucherCode);
            Double discount = restTemplate.getForObject(url, Double.class);
            return discount != null ? discount : 0.0;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("[Lỗi nghiệp vụ - Bỏ qua] Voucher không tồn tại hoặc nhập sai: {}", voucherCode);
            // Ném ngoại lệ nghiệp vụ -> Đã được cấu hình ignoreExceptions để KHÔNG làm ngắt Circuit Breaker
            throw new VoucherNotFoundException("Mã giảm giá '" + voucherCode + "' không tồn tại hoặc đã hết hạn!");
        }
    }

    public Double promoFallback(String voucherCode, Exception e) {
        if (e instanceof VoucherNotFoundException) {
            // Ném lại lỗi nghiệp vụ cho client mà không bị đè bởi Circuit Breaker fallback
            throw (VoucherNotFoundException) e;
        }
        log.error("[Circuit Breaker Fallback] Promo-Service gặp sự cố hạ tầng: {}. Không áp dụng voucher.", e.getMessage());
        return 0.0;
    }
}
