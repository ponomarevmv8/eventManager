package ponomarev.dev.eventmanager.location;

import org.springframework.stereotype.Component;

@Component
public class LocationConverterEntity {

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.id(),
                location.name(),
                location.address(),
                location.description(),
                location.capacity()
        );
    }

    public Location toDomain(LocationEntity location) {
        return new Location(
                location.getId(),
                location.getName(),
                location.getAddress(),
                location.getDescription(),
                location.getCapacity()
        );
    }
}
