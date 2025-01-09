package ponomarev.dev.eventmanager.location;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import ponomarev.dev.eventmanager.AbstractTest;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LocationControllerTest extends AbstractTest {

    @Autowired
    private LocationService locationService;


    @Test
    void shouldCreateLocation() throws Exception {
        Location locationToCreate = new Location(
                null,
                "test",
                "test address",
                "test description",
                2000
        );

        String locationToCreateJson = objectMapper.writeValueAsString(locationToCreate);

        String locationCreatedJson = mvc.perform(post("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(locationToCreateJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var locationCreated = objectMapper.readValue(locationCreatedJson, Location.class);

        Assertions.assertNotNull(locationCreated.id());

        Assertions.assertAll(
                () -> Assertions.assertEquals(locationToCreate.description(), locationCreated.description(), "Описание не совпадает"),
                () -> Assertions.assertEquals(locationToCreate.name(), locationCreated.name(), "Название location не совпадают"),
                () -> Assertions.assertEquals(locationToCreate.address(), locationCreated.address(), "Адресс location не совпадает"),
                () -> Assertions.assertEquals(locationToCreate.capacity(), locationCreated.capacity(), "Количество мест не совпадает")
        );
    }

    @Test
    void noShouldCreateLocation() throws Exception {
        Location locationToCreate = new Location(
                null,
                " ",
                "test",
                null,
                2
        );

        var locationToCreateJson = objectMapper.writeValueAsString(locationToCreate);

        mvc.perform(post("/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(locationToCreateJson))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldUpdateLocation() throws Exception {
        var locationCreated = locationService.create(
                new Location(
                        null,
                        "test",
                        "test address",
                        "test description",
                        2000
                )
        );

        var locationToUpdateJson = objectMapper.writeValueAsString(
                new Location(
                        locationCreated.id(),
                        "test new",
                        locationCreated.address(),
                        locationCreated.description(),
                        2500
                )
        );

        var locationUpdatedJson = mvc.perform(put("/locations/{id}", locationCreated.id())
                .contentType(MediaType.APPLICATION_JSON)
                .content(locationToUpdateJson)
        ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var locationUpdated = objectMapper.readValue(locationUpdatedJson, Location.class);

        Assertions.assertAll(
                () -> Assertions.assertEquals(locationUpdated.name(), "test new"),
                () -> Assertions.assertEquals(locationUpdated.capacity(), 2500),
                () -> Assertions.assertEquals(locationUpdated.description(), locationCreated.description()),
                () -> Assertions.assertEquals(locationUpdated.address(), locationCreated.address())
        );
    }

    @Test
    void notShouldUpdateLocation() throws Exception {
        var locationCreated = locationService.create(
                new Location(
                        null,
                        "test",
                        "test address",
                        "test description",
                        2000
                )
        );

        var locationToUpdateJson = objectMapper.writeValueAsString(
                new Location(
                        locationCreated.id(),
                        "   ",
                        locationCreated.address(),
                        locationCreated.description(),
                        2
                )
        );

        mvc.perform(put("/locations/{id}", locationCreated.id())
                .content(locationToUpdateJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldFindById() throws Exception {
        var locationCreated = locationService.create(
                new Location(
                        null,
                        "test",
                        "test address",
                        "test description",
                        2000
                )
        );

        var locationFindByIdJson = mvc.perform(get("/locations/{id}", locationCreated.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var locationFound = objectMapper.readValue(locationFindByIdJson, Location.class);

        org.assertj.core.api.Assertions.assertThat(locationFound)
                .usingRecursiveComparison()
                .isEqualTo(locationCreated);
    }

    @Test
    void noShouldFindById() throws Exception {
        mvc.perform(get("/locations/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void shouldDeleteLocation() throws Exception {
        var locationCreated = locationService.create(
                new Location(
                        null,
                        "test",
                        "test address",
                        "test description",
                        2000
                )
        );

        mvc.perform(delete("/locations/{id}", locationCreated.id()))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    void shouldNotDeleteLocation() throws Exception {
        mvc.perform(delete("/locations/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}
