package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.presentation.dto.LineRequest;
import subway.presentation.dto.StationEnrollRequest;
import subway.presentation.dto.StationRequest;
import subway.presentation.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하설 노선에 역 추가/삭제/조회 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("지하철 노선에 역을 추가할 수 있다")
    void test_addStationToLine() {
        //given
        LineRequest lineRequest = new LineRequest("2호선", "초록");

        long lineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        StationRequest jamsilRequest = new StationRequest("잠실역");
        StationRequest bangbaeRequest = new StationRequest("방배역");

        long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        //when, then
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/subway/{lineId}", lineId)

                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("지하철 노선에서 역을 삭제할 수 있다")
    void test_deleteStationFromLine() {
        //given
        LineRequest lineRequest = new LineRequest("2호선", "초록");

        long lineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        StationRequest jamsilRequest = new StationRequest("잠실역");
        StationRequest bangbaeRequest = new StationRequest("방배역");

        long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/subway/{lineId}", lineId);

        // when, then
        RestAssured.given()
                .when().delete("/subway/{lineId}/stations/{stationId}", lineId, jamsilStationId)

                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("지하철 노선의 역들을 조회할 수 있다.")
    void test_findStationFromLine() {
        //given
        LineRequest lineRequest = new LineRequest("2호선", "초록");

        long lineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        StationRequest jamsilRequest = new StationRequest("잠실역");
        StationRequest bangbaeRequest = new StationRequest("방배역");

        long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/subway/{lineId}", lineId);

        // when, then
        List<StationResponse> routeMap = RestAssured.given()
                .when().get("/subway/{lineId}", lineId)

                .then()
                .extract()
                .body()
                .jsonPath()
                .getList(".", StationResponse.class);

        assertAll(
                () -> assertThat(routeMap).hasSize(2),
                () -> assertThat(routeMap.get(0).getId()).isEqualTo(jamsilStationId),
                () -> assertThat(routeMap.get(0).getName()).isEqualTo("잠실역"),
                () -> assertThat(routeMap.get(1).getId()).isEqualTo(bangbaeStationId),
                () -> assertThat(routeMap.get(1).getName()).isEqualTo("방배역")
        );
    }

    @Test
    @DisplayName("모든 지하철 노선의 역들을 조회할 수 있다.")
    void test_findAllStationFromLine() {
        //given
        LineRequest lineOneRequest = new LineRequest("1호선", "파랑");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineOneRequest)

                .when().post("/lines");

        LineRequest lineTwoRequest = new LineRequest("2호선", "초록");

        long lineTwoId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineTwoRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        StationRequest jamsilRequest = new StationRequest("잠실역");
        StationRequest bangbaeRequest = new StationRequest("방배역");

        long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/subway/{lineId}", lineTwoId);

        // when, then
        RestAssured.given()
                .when().get("/subway")

                .then()
                .body("size()", is(2));
    }
}
