package subway.integration.graph;

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

public class GetShortestPathTest extends IntegrationTestSetUp {

    private final LineCreateRequest createRequest1 =
            new LineCreateRequest("3호선", "A", "B", 9);
    private final LineCreateRequest createRequest2 =
            new LineCreateRequest("2호선", "A", "B", 10);

    @DisplayName("지하철 노선을 생성후 역을 추가한다. 3호선 : A-B-C")
    @BeforeEach
    void init() {
        super.setUp();

        // 3호선 : A-B-C
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest1)
                .when().post("/lines")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");

        final AddStationToLineRequest request1 = new AddStationToLineRequest(2L, "C", Direction.DOWN, 30);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request1)
                .when().post("/lines/{lineId}/stations", 1)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/1");

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(1))
                .body("lineName", equalTo("3호선"))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("B"))
                .body("stations[2].stationName", equalTo("C"));

        // 2호선 : A-B-D
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest2)
                .when().post("/lines")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/2");

        final AddStationToLineRequest request2 = new AddStationToLineRequest(2L, "D", Direction.DOWN, 15);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request2)
                .when().post("/lines/{lineId}/stations", 2)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/lines/2");

        // then
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 2)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("lineId", equalTo(2))
                .body("lineName", equalTo("2호선"))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("B"))
                .body("stations[2].stationName", equalTo("D"));
    }

    @Test
    void test() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/path/shortest?fromId=1&toId=4")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("totalDistance", equalTo(24))
                .body("stations[0].stationName", equalTo("A"))
                .body("stations[1].stationName", equalTo("B"))
                .body("stations[2].stationName", equalTo("D"))
                .body("fare", equalTo(1550));
    }
}
