package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.StationCreateRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationIntegrationTest {
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
        jdbcTemplate.update("TRUNCATE TABLE station");
        jdbcTemplate.update("TRUNCATE TABLE line");
        jdbcTemplate.update("TRUNCATE TABLE section");
    }

    @Test
    void createStationTest() {
        final StationCreateRequest stationCreateRequest = new StationCreateRequest("사평역");

        final ExtractableResponse<Response> response = RestAssured
                .given()
                .log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(stationCreateRequest)

                .when()
                .post("/stations")

                .then()
                .log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }
}
