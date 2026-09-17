package microservice.orderservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerEmail;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    private Double totalAmount;
    private String status;

    private LocalDateTime createdAt;

    public void calculateTotalAmount() {
        if (items != null && !items.isEmpty()) {
            this.totalAmount = items.stream()
                    .mapToDouble(item -> (item.getUnitPrice() != null ? item.getUnitPrice() : 0.0) * (item.getQuantity() != null ? item.getQuantity() : 1))
                    .sum();
        } else if (this.totalAmount == null) {
            this.totalAmount = 0.0;
        }
    }

    @PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null || status.isBlank()) {
            status = "PENDING";
        }
        calculateTotalAmount();
    }
}
