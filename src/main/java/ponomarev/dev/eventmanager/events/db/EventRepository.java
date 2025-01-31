package ponomarev.dev.eventmanager.events.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Modifying
    @Transactional
    @Query("""
            update EventEntity e set e.status = :status where e.id = :id
            """)
    void updateStatus(
            @Param("id") Long id,
            @Param("status") String status);

    @Query("""
             select e from EventEntity e
             where (:name is null or e.name = :name)
             and ((:placesMin is null or e.maxPlaces > :placesMin)
             and (:placesMax is null or e.maxPlaces < :placesMax))
             and ((CAST(:dateStartAfter as date) is null or e.date > :dateStartAfter )
              and (CAST(:dateStartBefore as date) is null or e.date < :dateStartBefore ))
             and ((:costMin is null or e.cost > :costMin)
              and (:costMax is null or e.cost < :costMax))
             and ((:durationMin is null or e.duration > :durationMin)
              and (:durationMax is null or e.duration < :durationMax))
             and (:locationId is null or e.locationId = :locationId)
              and (:eventStatus is null or e.status = :eventStatus)
            """)
    List<EventEntity> searchEvents(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") Integer costMin,
            @Param("costMax") Integer costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") String eventStatus

    );

    @Query("""
            select e from EventEntity e
            left join fetch e.eventParticipantList ep
            where e.ownerId = :ownerId
            """)
    List<EventEntity> findByOwnerId(@Param("ownerId") Long ownerId);

    @Query(value = """
            select e.* from events e
            where e.date < now()
            and (e.date + e.duration* INTERVAL '1 minute') > now()
            and e.status = 'WAIT_START'
            """, nativeQuery = true)
    List<EventEntity> findStartedWithStatus();

    @Query(value = """
            select e.* from events e 
            where e.status = 'STARTED'
            and (e.date + e.duration* INTERVAL '1 minute') < now()
            """, nativeQuery = true)
    List<EventEntity> findEndedWithStatus();

    @Modifying
    @Query("""
            update EventEntity e set e.status = :status where e.id in :ids
            """)
    @Transactional
    void updateStatusAll(
            @Param("status") String status,
            @Param("ids") List<Long> ids);
}
