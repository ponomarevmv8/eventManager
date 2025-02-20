package ponomarev.dev.eventmanager.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ponomarev.dev.eventmanager.events.domain.Event;
import ponomarev.dev.eventmanager.events.domain.EventParticipant;
import ponomarev.dev.eventmanager.events.domain.EventStatus;
import ponomarev.dev.eventmanager.kafka.event.EventChangeKafkaEvent;
import ponomarev.dev.eventmanager.kafka.event.FieldChange;
import ponomarev.dev.eventmanager.user.domain.User;

import java.util.function.Function;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final EventKafkaSender eventKafkaSender;

    public NotificationService(EventKafkaSender eventKafkaSender) {
        this.eventKafkaSender = eventKafkaSender;
    }

    public void sendUpdateFieldEvent(Event updateEvent, Event oldEvent, User changeUser) {

        var kafkaMessage = new EventChangeKafkaEvent(
                updateEvent.id(),
                changeUser.id(),
                updateEvent.ownerId(),
                checkUpdatedField(oldEvent, updateEvent, Event::name),
                checkUpdatedField(oldEvent, updateEvent, Event::maxPlaces),
                checkUpdatedField(oldEvent, updateEvent, Event::date),
                checkUpdatedField(oldEvent, updateEvent, Event::cost),
                checkUpdatedField(oldEvent, updateEvent, Event::duration),
                checkUpdatedField(oldEvent, updateEvent, Event::locationId),
                checkUpdatedField(oldEvent, updateEvent, Event::status),
                updateEvent.eventParticipantList()
                        .stream()
                        .map(EventParticipant::userId)
                        .toList()
        );

        eventKafkaSender.send(kafkaMessage);

        log.info("Kafka event sent: {}", kafkaMessage);
    }

    public void sendUpdateStatusEvent(Event updateEvent, EventStatus newStatus, User changeUser) {
        var kafkaMessage = new EventChangeKafkaEvent(
                updateEvent.id(),
                changeUser.id(),
                updateEvent.ownerId(),
                updateEvent.eventParticipantList()
                        .stream()
                        .map(EventParticipant::userId)
                        .toList()
        );

        kafkaMessage.setStatus(
                new FieldChange<>(
                        updateEvent.status(),
                        newStatus
                )
        );

        eventKafkaSender.send(kafkaMessage);

        log.info("Kafka event sent: {}", kafkaMessage);
    }

    private <R> FieldChange<R> checkUpdatedField(Event oldEvent, Event updatedEvent, Function<Event, R> field) {
        R oldValue = field.apply(oldEvent);
        R newValue = field.apply(updatedEvent);
        if(!oldValue.equals(newValue)) {
            return new FieldChange<>(
                    oldValue,
                    newValue
            );
        }
        return null;
    }


}
