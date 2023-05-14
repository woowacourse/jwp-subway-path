package subway.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.line.Direction;
import subway.ui.line.dto.AddStationToLineRequest;
import subway.ui.line.dto.LineCreateRequest;

import static org.hamcrest.Matchers.is;

public class DeleteStationFromLineTest extends IntegrationTestSetUp {

    private final LineCreateRequest createRequest =
            new LineCreateRequest("3호선", "A", "B", 10);

    @DisplayName("3호선에 A-B-C 역이 있을 때 - ")
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

        final AddStationToLineRequest request = new AddStationToLineRequest(2L, "C", Direction.DOWN, 3);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/stations", 1)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");
    }

    @DisplayName("A를 제거한다.")
    @Test
    void deleteStationFromLine1() {
        // when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", 1, 1)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", is(1))
                .body("lineName", is("3호선"))
                .body("stations[0].stationName", is("B"))
                .body("stations[1].stationName", is("C"));
    }

    @DisplayName("B를 제거한다.")
    @Test
    void deleteStationFromLine2() {
        // when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", 1, 2)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", is(1))
                .body("lineName", is("3호선"))
                .body("stations[0].stationName", is("A"))
                .body("stations[1].stationName", is("C"));
    }

    @DisplayName("C를 제거한다.")
    @Test
    void deleteStationFromLine3() {
        // when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", 1, 3)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", is(1))
                .body("lineName", is("3호선"))
                .body("stations[0].stationName", is("A"))
                .body("stations[1].stationName", is("B"));
    }
}
