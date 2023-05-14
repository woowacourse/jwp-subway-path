package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.line.Direction;
import subway.domain.line.dto.AddStationToLineRequest;
import subway.domain.line.dto.LineCreateRequest;

import static org.hamcrest.Matchers.equalTo;

public class AddStationToLineTest extends IntegrationTestSetUp {

    private final LineCreateRequest createRequest =
            new LineCreateRequest("3호선", "A", "B", 10);

    @DisplayName("3호선에 A-B 역이 있을 때 - ")
    @BeforeEach
    void init() {
        super.setUp();
        // given
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all()
                .header("Location", "/lines/1");
    }

    @DisplayName("B-C를 DOWN 방향으로 추가하면 A-B-C가 된다.")
    @Test
    void addStationToLine1() {
        // when
        final AddStationToLineRequest request = new AddStationToLineRequest(2L, "C", Direction.DOWN, 3);
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
                .body("stations[1].stationName", equalTo("B"))
                .body("stations[2].stationName", equalTo("C"));
    }

    @DisplayName("B-C를 UP 방향으로 추가하면 A-C-B가 된다.")
    @Test
    void addStationToLine2() {
        // when
        final AddStationToLineRequest request = new AddStationToLineRequest(2L, "C", Direction.UP, 3);
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

    @DisplayName("A-C를 DOWN 방향으로 추가하면 A-C-B가 된다.")
    @Test
    void addStationToLine3() {
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

    @DisplayName("A-C를 UP 방향으로 추가하면 C-A-B가 된다.")
    @Test
    void addStationToLine4() {
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