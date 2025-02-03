package ponomarev.dev.eventmanager.events.db;

import jakarta.persistence.*;


@Entity
@Table(name = "participants")
public class EventParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventEntity event;

    public EventParticipantEntity() {
    }

    public EventParticipantEntity(Long id, Long userId, EventEntity event) {
        this.id = id;
        this.userId = userId;
        this.event = event;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }
}
