package ponomarev.dev.eventmanager.events.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ponomarev.dev.eventmanager.events.db.EventEntity;
import ponomarev.dev.eventmanager.events.db.EventRepository;


@Configuration
@EnableScheduling
public class EventStatusUpdater {


    private static final Logger log = LoggerFactory.getLogger(EventStatusUpdater.class);
    private final EventRepository eventRepository;

    @Value("${scheduled.update.fixedrate}")
    private static Long fixedRate;

    public EventStatusUpdater(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(fixedRateString = "#{${scheduled.update.fixedrate}}")
    public void updateStatusEvents() {
        log.info("Updating status events");
        var startsEvent = eventRepository.findStartedWithStatus();

        if(!startsEvent.isEmpty()) {
            eventRepository.updateStatusAll(EventStatus.STARTED.name(),
                    startsEvent.stream().map(EventEntity::getId).toList());
        }

        var endsEvent = eventRepository.findEndedWithStatus();
        if(!endsEvent.isEmpty()) {
            eventRepository.updateStatusAll(
                    EventStatus.FINISHED.name(),
                    endsEvent.stream().map(EventEntity::getId).toList()
            );
        }
    }

}
