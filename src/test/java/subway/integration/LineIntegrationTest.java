package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@DisplayName("지하철 노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {

    @DisplayName("전체 노선을 조회 한다")
    @Test
    void findAllLines() {
        given()
                .log().all()

        .when()
                .get("/lines/stations")

        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);

    }

    @DisplayName("노선을 조회 한다")
    @Test
    void findLineStations() {
        given()
                .log().all()

        .when()
                .get("/lines/{lineName}/stations", "1")

        .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
