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


public class FindLineTest extends IntegrationTestSetUp {

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

    @Test
    @DisplayName("특정 노선의 모든 역을 조회한다.")
    void findLineById() {
        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", is(1))
                .body("lineName", is("3호선"))
                .body("stations[0].stationName", is("A"))
                .body("stations[1].stationName", is("B"))
                .body("stations[2].stationName", is("C"));
    }

    @Test
    @DisplayName("모든 노선의 모든 역을 조회한다")
    void findAllLines() {

        final LineCreateRequest createRequest =
                new LineCreateRequest("1호선", "X", "Y", 10);

        // given, when
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest)
                .when().post("/lines")
                .then().log().all()
                .header("Location", "/lines/2");

        final AddStationToLineRequest request = new AddStationToLineRequest(5L, "Z", Direction.DOWN, 3);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/{lineId}/stations", 2)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/2");

        // then
        RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("lines[0].lineId", is(1))
                .body("lines[0].lineName", is("3호선"))
                .body("lines[0].stations[0].stationName", is("A"))
                .body("lines[0].stations[1].stationName", is("B"))
                .body("lines[0].stations[2].stationName", is("C"))
                .body("lines[1].lineId", is(2))
                .body("lines[1].lineName", is("1호선"))
                .body("lines[1].stations[0].stationName", is("X"))
                .body("lines[1].stations[1].stationName", is("Y"))
                .body("lines[1].stations[2].stationName", is("Z"));
    }
}
