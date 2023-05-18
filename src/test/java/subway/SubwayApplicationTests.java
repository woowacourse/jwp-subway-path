package subway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.config.location = classpath:application.yml")
class SubwayApplicationTests {

	@Test
	void contextLoads() {
	}

}
