package microservice.marketingservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Voucher {
    private String voucherCode;
    private Double discount;
    private String description;
}
