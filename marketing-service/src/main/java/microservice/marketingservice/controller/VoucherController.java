package microservice.marketingservice.controller;


import lombok.RequiredArgsConstructor;
import microservice.marketingservice.model.Voucher;
import microservice.marketingservice.service.VoucherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;


    @GetMapping("/{number}")
    public ResponseEntity<List<Voucher>> getVouchers(@PathVariable int number){
        return ResponseEntity.ok(voucherService.getAllVoucher(number));
    }
}
