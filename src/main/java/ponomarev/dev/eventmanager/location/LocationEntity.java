package ponomarev.dev.eventmanager.location;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    public LocationEntity() {
    }

    public LocationEntity(Long id, String name, String address, String description, Integer capacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.capacity = capacity;
    }

    public LocationEntity(Integer capacity, String address, Long id, String name) {
        this.capacity = capacity;
        this.address = address;
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
