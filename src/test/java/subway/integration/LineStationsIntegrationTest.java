package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.SectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/schema.sql")
@DisplayName("LineStationsController는 ")
class LineStationsIntegrationTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("노선을 순서대로 조회할 수 있다.")
    @Test
    void readLineStations() {
        long _2호선 = 2L;

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(String.format("/line-stations/%d", _2호선))
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("전체 노선을 순서대로 조회할 수 있다.")
    @Test
    void readAllLinesStations() {
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line-stations")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
