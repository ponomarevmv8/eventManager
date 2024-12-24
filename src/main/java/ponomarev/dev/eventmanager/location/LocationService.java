package ponomarev.dev.eventmanager.location;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationConverterEntity converter;

    public LocationService(LocationRepository locationRepository, LocationConverterEntity converter) {
        this.locationRepository = locationRepository;
        this.converter = converter;
    }

    public Location findById(Long id) {
        return converter.toDomain(locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found location with id: %s".formatted(id))));
    }

    public List<Location> findAll() {
        return locationRepository.findAll()
                .stream()
                .map(converter::toDomain)
                .toList();
    }

    public Location create(Location location) {
        var locationToCreate = converter.toEntity(location);
        locationToCreate.setId(null);
        var locationCreate = locationRepository.save(locationToCreate);
        return converter.toDomain(locationCreate);
    }

    public Location update(Long id, Location location) {
        if(!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found location with id: %s".formatted(id));
        }
        var locationUpdated = converter.toEntity(location);
        locationUpdated.setId(id);
        return converter.toDomain(locationRepository.save(locationUpdated));
    }

    public void delete(Long id) {
        if(!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found location with id: %s".formatted(id));
        }
        locationRepository.deleteById(id);
    }

}
