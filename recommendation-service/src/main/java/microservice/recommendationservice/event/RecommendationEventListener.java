package microservice.recommendationservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecommendationEventListener {

    @Async
    @EventListener
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("[Async Event Received] Recommendation-Service received OrderCreatedEvent: OrderId={}, Customer={}, ProductIds={}, TotalAmount={}",
                event.getOrderId(), event.getCustomerName(),
                event.getProductIds() != null ? event.getProductIds() : event.getProductId(),
                event.getTotalAmount());
    }
}
