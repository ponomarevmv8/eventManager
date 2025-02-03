package ponomarev.dev.eventmanager.events.db;

import jakarta.persistence.*;
import ponomarev.dev.eventmanager.events.domain.EventStatus;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @OneToMany(mappedBy = "event")
    private List<EventParticipantEntity> eventParticipantList;

    @Column(name = "status", nullable = false)
    private EventStatus status;

    public EventEntity() {
    }

    public EventEntity(Long id, String name, String description, Long ownerId, Integer maxPlaces, LocalDateTime date, Integer cost, Integer duration, Long locationId, List<EventParticipantEntity> eventParticipantList, EventStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.eventParticipantList = eventParticipantList;
        this.status = status;
    }



    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public List<EventParticipantEntity> getEventParticipantList() {
        return eventParticipantList;
    }

    public void setEventParticipantList(List<EventParticipantEntity> eventParticipantList) {
        this.eventParticipantList = eventParticipantList;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getLocationId() {
        return locationId;
    }
}