package ponomarev.dev.eventmanager.events.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ponomarev.dev.eventmanager.events.db.EventEntity;
import ponomarev.dev.eventmanager.events.db.EventParticipantEntity;
import ponomarev.dev.eventmanager.events.db.EventRepository;
import ponomarev.dev.eventmanager.kafka.EventKafkaSender;
import ponomarev.dev.eventmanager.kafka.event.EventChangeKafkaEvent;
import ponomarev.dev.eventmanager.kafka.event.FieldChange;


@Configuration
@EnableScheduling
public class EventStatusUpdater {


    private static final Logger log = LoggerFactory.getLogger(EventStatusUpdater.class);
    private final EventRepository eventRepository;
    private final EventKafkaSender eventKafkaSender;

    @Value("${scheduled.update.fixedrate}")
    private static Long fixedRate;

    public EventStatusUpdater(EventRepository eventRepository, EventKafkaSender eventKafkaSender) {
        this.eventRepository = eventRepository;
        this.eventKafkaSender = eventKafkaSender;
    }

    @Scheduled(fixedRateString = "#{${scheduled.update.fixedrate}}")
    public void updateStatusEvents() {
        log.info("Updating status events");
        var startsEvent = eventRepository.findStartedWithStatus();

        if(!startsEvent.isEmpty()) {
            startsEvent.forEach(
                    event -> {
                        var kafkaMessage = new EventChangeKafkaEvent(
                                event.getId(),
                                null,
                                event.getOwnerId(),
                                event.getEventParticipantList()
                                        .stream()
                                        .map(EventParticipantEntity::getUserId)
                                        .toList()
                        );
                        kafkaMessage.setStatus(
                                new FieldChange<>(
                                        EventStatus.valueOf(event.getStatus()),
                                        EventStatus.STARTED
                                )
                        );
                        eventKafkaSender.send(kafkaMessage);
                        log.info("Sent message to kafka {}", kafkaMessage);
                    }
            );
            eventRepository.updateStatusAll(EventStatus.STARTED.name(),
                    startsEvent.stream().map(EventEntity::getId).toList());
        }

        var endsEvent = eventRepository.findEndedWithStatus();
        if(!endsEvent.isEmpty()) {
            eventRepository.updateStatusAll(
                    EventStatus.FINISHED.name(),
                    endsEvent.stream().map(EventEntity::getId).toList()
            );
            endsEvent.forEach(
                    event -> {
                        var kafkaMessage = new EventChangeKafkaEvent(
                                event.getId(),
                                null,
                                event.getOwnerId(),
                                event.getEventParticipantList()
                                        .stream()
                                        .map(EventParticipantEntity::getUserId)
                                        .toList()
                        );
                        kafkaMessage.setStatus(
                                new FieldChange<>(
                                        EventStatus.valueOf(event.getStatus()),
                                        EventStatus.FINISHED
                                )
                        );
                        eventKafkaSender.send(kafkaMessage);
                        log.info("Sent message to kafka {}", kafkaMessage);
                    }
            );
        }
    }

}
