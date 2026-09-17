package microservice.orderservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("[Async Event Published] Publishing OrderCreatedEvent for OrderId={}", event.getOrderId());
        eventPublisher.publishEvent(event);
    }
}
