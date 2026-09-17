package microservice.orderservice;

import microservice.orderservice.model.Order;
import microservice.orderservice.model.OrderItem;
import microservice.orderservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initOrderData(OrderRepository orderRepository) {
        return args -> {
            Order order1 = Order.builder()
                    .customerName("Nguyễn Văn A")
                    .customerEmail("anv@gmail.com")
                    .status("PENDING")
                    .createdAt(LocalDateTime.now().minusHours(2))
                    .items(List.of(
                            OrderItem.builder()
                                    .productId(101L)
                                    .productName("iPhone 15 Pro Max")
                                    .quantity(1)
                                    .unitPrice(22990000.0)
                                    .build()
                    ))
                    .build();
            order1.calculateTotalAmount();

            Order order2 = Order.builder()
                    .customerName("Trần Thị B")
                    .customerEmail("btt@gmail.com")
                    .status("PAID")
                    .createdAt(LocalDateTime.now().minusHours(1))
                    .items(List.of(
                            OrderItem.builder()
                                    .productId(102L)
                                    .productName("MacBook Pro M3")
                                    .quantity(1)
                                    .unitPrice(45990000.0)
                                    .build(),
                            OrderItem.builder()
                                    .productId(101L)
                                    .productName("iPhone 15 Pro Max")
                                    .quantity(1)
                                    .unitPrice(22990000.0)
                                    .build()
                    ))
                    .build();
            order2.calculateTotalAmount();

            orderRepository.saveAll(List.of(order1, order2));
        };
    }
}
