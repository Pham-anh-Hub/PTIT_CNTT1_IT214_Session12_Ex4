package microservice.marketingservice.service;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import microservice.marketingservice.model.Voucher;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VoucherService {

    @CircuitBreaker(name = "voucherCircuitBreaker", fallbackMethod = "getAllVoucherFallback")
    public List<Voucher> getAllVoucher(int number){
        if (number % 2 != 0){
            throw new RuntimeException("Không có voucher hiện tồn tại");
        }

        return List.of(Voucher.builder().voucherCode("VC-01").discount(0.55).description("Mã giảm giá").build(),
                Voucher.builder().voucherCode("VC-03").discount(0.2).description("Mã giảm giá").build(),
                Voucher.builder().voucherCode("VC-02").discount(0.45).description("Mã giảm giá").build());
    }

    public List<Voucher> getAllVoucherFallback(int number, Throwable throwable){
        log.error("Lỗi lấy danh sách voucher cho number = {}: {}", number, throwable.getMessage());
        return List.of(Voucher.builder().voucherCode("DEFAULT-FREESHIP").discount(0.15).description("Freeship cho mọi đơn hàng (hệ thống dự phòng)").build());
    }
}
