package subway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@Sql("/initialization.sql")
@SpringBootTest
class SubwayApplicationTests {

	@Test
	void contextLoads() {
	}

}
