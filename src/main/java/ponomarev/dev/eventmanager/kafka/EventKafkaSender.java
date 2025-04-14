package ponomarev.dev.eventmanager.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ponomarev.dev.eventmanager.kafka.event.EventChangeKafkaEvent;

@Component
public class EventKafkaSender {


    private static final Logger log = LoggerFactory.getLogger(EventKafkaSender.class);
    private final KafkaTemplate<Long, EventChangeKafkaEvent> kafkaTemplate;

    public EventKafkaSender(KafkaTemplate<Long, EventChangeKafkaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(EventChangeKafkaEvent event) {
        log.info("Sending event to kafka {} ", event);
        kafkaTemplate.send(
                "event-change",
                event.getUserChangeBy(),
                event
        );
    }
}
