package subway.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

@DisplayName("지하철역 관련 기능")
class StationIntegrationTest extends IntegrationTest {

    @DisplayName("역을 저장한다.")
    @Test
    void createStation() {
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "    \"upStationName\":\"헤나\",\n" +
                        "    \"downStationName\":\"지토\",\n" +
                        "    \"lineName\":\"1\",\n" +
                        "    \"lineColor\":\"파랑\",\n" +
                        "    \"distance\":5\n" +
                        "}")

        .when()
                .post("/stations")

        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void deleteStationByName() {
        given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" +
                        "    \"stationName\": \"헤나\",\n" +
                        "    \"lineName\" : \"1\"\n" +
                        "}")

        .when()
                .delete("/stations")

        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
