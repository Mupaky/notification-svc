package app.event;

import app.event.payload.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisteredEventConsumer {

//    @KafkaListener(topics = "notification-topic.v1", groupId = "notification-svc")
//    public void consumeEvent(UserRegisteredEvent event) {
//        log.info("Received/Consumed UserRegisteredEvent from Kafka topic: {}", event);
//    }
}
