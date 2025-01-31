package ponomarev.dev.eventmanager.events.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipantEntity, Long> {

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Optional<EventParticipantEntity> findByUserIdAndEventId(Long userId, Long eventId);

    @Query("""
            select p from EventParticipantEntity p
            join fetch p.event
            where p.userId=:userId
            """)
    List<EventParticipantEntity> findAllByUserId(
            @Param("userId") Long userId);

}
