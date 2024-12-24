package ponomarev.dev.eventmanager.location;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private static final Logger log = LoggerFactory.getLogger(LocationController.class);
    private final LocationService locationService;
    private final LocationConverterDto converter;

    public LocationController(LocationService locationService, LocationConverterDto converter) {
        this.locationService = locationService;
        this.converter = converter;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> findById(@PathVariable Long id) {
        log.info("Find location by id: {}", id);
        return ResponseEntity.ok(converter.toDto(locationService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> findAll() {
        log.info("Find all locations");
        return ResponseEntity.ok(locationService.findAll().stream()
                .map(converter::toDto)
                .toList());
    }

    @PostMapping
    public ResponseEntity<LocationDto> create(@RequestBody @Valid LocationDto locationDto) {
        log.info("Create location: {}", locationDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(converter.toDto(
                        locationService.create(converter.toDomain(locationDto))
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> update(@PathVariable Long id,
                                              @RequestBody @Valid LocationDto locationDto) {
        log.info("Update location: {}", locationDto);
        return ResponseEntity.ok(converter.toDto(
                locationService.update(id,
                        converter.toDomain(locationDto))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Delete location: {}", id);
        locationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
