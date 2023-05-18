package subway.integration.line;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.section.Direction;
import subway.integration.IntegrationTestSetUp;
import subway.ui.line.dto.AddStationToLineRequest;
import subway.ui.line.dto.LineCreateRequest;

import static org.hamcrest.Matchers.equalTo;

public class AddStationToLineTest extends IntegrationTestSetUp {

    private final LineCreateRequest createRequest =
            new LineCreateRequest("3호선", "A", "B", 10);

    @DisplayName("3호선에 A-B 역이 있을 때 - ")
    @BeforeEach
    void init() {
        super.setUp();
        // given
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then()
                .header("Location", "/lines/1");
    }

    @DisplayName("B-C를 DOWN 방향으로 추가하면 A-B-C가 된다.")
    @Test
    void addStationToLine1_success() {
        // when
        final AddStationToLineRequest request = new AddStationToLineRequest(2L, "C", Direction.DOWN, 3);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request).log().all()
                .when().post("/lines/{lineId}/stations", 1)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(1))
                .body("lineName", equalTo("3호선"))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("B"))
                .body("stations[2].stationName", equalTo("C"));
    }

    @DisplayName("B-C를 UP 방향으로 추가하면 A-C-B가 된다.")
    @Test
    void addStationToLine2_success() {
        // when
        final AddStationToLineRequest request = new AddStationToLineRequest(2L, "C", Direction.UP, 3);
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request).log().all()
                .when().post("/lines/{lineId}/stations", 1)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(1))
                .body("lineName", equalTo("3호선"))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("C"))
                .body("stations[2].stationName", equalTo("B"));
    }

    @DisplayName("B-C를 UP 방향으로 추가하되, distance가 기존의 section보다 길다면 BAD_REQUEST가 반환된다.")
    @Test
    void addStationToLine2_fail() {
        // when
        final AddStationToLineRequest request = new AddStationToLineRequest(2L, "C", Direction.UP, 10);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/stations", 1)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(1))
                .body("lineName", equalTo("3호선"))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("B"));
    }

    @DisplayName("A-C를 DOWN 방향으로 추가하면 A-C-B가 된다.")
    @Test
    void addStationToLine3_success() {
        // when
        final AddStationToLineRequest request = new AddStationToLineRequest(1L, "C", Direction.DOWN, 3);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/stations", 1)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(1))
                .body("lineName", equalTo("3호선"))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("C"))
                .body("stations[2].stationName", equalTo("B"));
    }

    @DisplayName("A-C를 DOWN 방향으로 추가추가하되, distance가 기존의 section보다 길다면 BAD_REQUEST가 반환된다.")
    @Test
    void addStationToLine3_fail() {
        final AddStationToLineRequest request = new AddStationToLineRequest(1L, "C", Direction.DOWN, 11);
        // when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/stations", 1)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(1))
                .body("lineName", equalTo("3호선"))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("B"));
    }

    @DisplayName("A-C를 UP 방향으로 추가하면 C-A-B가 된다.")
    @Test
    void addStationToLine4_success() {
        // when
        final AddStationToLineRequest request = new AddStationToLineRequest(1L, "C", Direction.UP, 3);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/stations", 1)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(1))
                .body("lineName", equalTo("3호선"))
                .body("stations[0].stationName", equalTo("C"))
                .body("stations[1].stationName", equalTo("A"))
                .body("stations[2].stationName", equalTo("B"));
    }
}
