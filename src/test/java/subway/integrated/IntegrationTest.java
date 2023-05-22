package subway.integrated;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

import static constant.TestConstants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {
    @LocalServerPort
    int port;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }
    
    @AfterEach
    void tearDown() {
        jdbcTemplate.execute(SECTION_DELETE_ALL_SQL);
        jdbcTemplate.execute(STATION_DELETE_ALL_SQL);
        jdbcTemplate.execute(LINE_DELETE_ALL_SQL);
    }
}
