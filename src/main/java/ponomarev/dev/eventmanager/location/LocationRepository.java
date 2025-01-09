package ponomarev.dev.eventmanager.location;


import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
}
