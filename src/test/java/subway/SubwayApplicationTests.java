package subway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import subway.config.TestConfiguration;

@ActiveProfiles("test")
@ContextConfiguration(classes = TestConfiguration.class)
@SpringBootTest
class SubwayApplicationTests {

    @Test
    void contextLoads() {
    }

}
