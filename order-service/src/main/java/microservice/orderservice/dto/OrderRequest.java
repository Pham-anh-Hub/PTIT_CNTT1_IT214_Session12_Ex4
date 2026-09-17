package microservice.orderservice.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private String customerName;
    private String customerEmail;
    private Long userId;
    
    // Hỗ trợ truyền danh sách nhiều sản phẩm
    private List<OrderItemRequest> items;

    // Hỗ trợ truyền sản phẩm đơn lẻ ngắn gọn
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;

    // Không bắt buộc truyền totalAmount, nếu bỏ trống hệ thống sẽ tự động tính = sum(quantity * unitPrice)
    private Double totalAmount;
    private String status;
}
