package microservice.orderservice.event;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent implements Serializable {
    private Long orderId;
    private String customerName;
    private List<Long> productIds;
    private Double totalAmount;
    private String status;
}
