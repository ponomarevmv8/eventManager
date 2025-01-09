package ponomarev.dev.eventmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AbstractTest {

    private static volatile boolean isSharedSetupDone = false;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    public static PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:15.3")
                    .withPassword("root")
                    .withUsername("postgres")
                    .withDatabaseName("postgres");

    static {
        if (!isSharedSetupDone) {
            POSTGRES_CONTAINER.start();
            isSharedSetupDone = true;
        }
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("test.postgres.port", POSTGRES_CONTAINER::getFirstMappedPort);
    }

    @EventListener
    public void stopContainer(ContextStoppedEvent event) {
        POSTGRES_CONTAINER.stop();
    }

}
