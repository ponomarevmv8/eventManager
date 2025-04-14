package ponomarev.dev.eventmanager.kafka.event;

import ponomarev.dev.eventmanager.events.domain.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public class EventChangeKafkaEvent {
    private Long eventId;
    private Long userChangeBy;
    private Long ownerId;
    private FieldChange<String> name;
    private FieldChange<Integer> maxPlace;
    private FieldChange<LocalDateTime> date;
    private FieldChange<Integer> cost;
    private FieldChange<Integer> duration;
    private FieldChange<Long> locationId;
    private FieldChange<EventStatus> status;
    private List<Long> participantList;

    public EventChangeKafkaEvent() {
    }

    public EventChangeKafkaEvent(Long eventId, Long userChangeBy, Long ownerId, List<Long> participantList) {
        this.eventId = eventId;
        this.userChangeBy = userChangeBy;
        this.ownerId = ownerId;
        this.participantList = participantList;
    }

    public EventChangeKafkaEvent(Long eventId, Long userChangeBy, Long ownerId, FieldChange<String> name, FieldChange<Integer> maxPlace, FieldChange<LocalDateTime> date, FieldChange<Integer> cost, FieldChange<Integer> duration, FieldChange<Long> locationId, FieldChange<EventStatus> status, List<Long> participantList) {
        this.eventId = eventId;
        this.userChangeBy = userChangeBy;
        this.ownerId = ownerId;
        this.name = name;
        this.maxPlace = maxPlace;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
        this.participantList = participantList;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getUserChangeBy() {
        return userChangeBy;
    }

    public void setUserChangeBy(Long userChangeBy) {
        this.userChangeBy = userChangeBy;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public FieldChange<String> getName() {
        return name;
    }

    public void setName(FieldChange<String> name) {
        this.name = name;
    }

    public FieldChange<Integer> getMaxPlace() {
        return maxPlace;
    }

    public void setMaxPlace(FieldChange<Integer> maxPlace) {
        this.maxPlace = maxPlace;
    }

    public FieldChange<LocalDateTime> getDate() {
        return date;
    }

    public void setDate(FieldChange<LocalDateTime> date) {
        this.date = date;
    }

    public FieldChange<Integer> getCost() {
        return cost;
    }

    public void setCost(FieldChange<Integer> cost) {
        this.cost = cost;
    }

    public FieldChange<Integer> getDuration() {
        return duration;
    }

    public void setDuration(FieldChange<Integer> duration) {
        this.duration = duration;
    }

    public FieldChange<Long> getLocationId() {
        return locationId;
    }

    public void setLocationId(FieldChange<Long> locationId) {
        this.locationId = locationId;
    }

    public List<Long> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<Long> participantList) {
        this.participantList = participantList;
    }

    public FieldChange<EventStatus> getStatus() {
        return status;
    }

    public void setStatus(FieldChange<EventStatus> status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EventChangeKafkaEvent{" +
               "eventId=" + eventId +
               ", userChangeBy=" + userChangeBy +
               ", ownerId=" + ownerId +
               ", name=" + name +
               ", maxPlace=" + maxPlace +
               ", date=" + date +
               ", cost=" + cost +
               ", duration=" + duration +
               ", locationId=" + locationId +
               ", status=" + status +
               ", participantList=" + participantList +
               '}';
    }
}
