package subway.ui;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.application.LineService;
import subway.application.StationService;
import subway.dto.LineRequest;
import subway.dto.PathRequest;
import subway.dto.StationRequest;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StationService stationService;
    @Autowired
    private LineService lineService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("한 노선에서 최단 경로를 조회할 수 있다")
    @Test
    void getDijkstraShortestPath() {
        //given
        Long lineId = lineService.saveLine(new LineRequest("1호선", "red")).getId();
        Long stationId1 = stationService.saveStation(new StationRequest("광안역")).getId();
        Long stationId2 = stationService.saveStation(new StationRequest("전포역")).getId();
        Long stationId3 = stationService.saveStation(new StationRequest("노포역")).getId();
        lineService.addPathToLine(lineId, new PathRequest(stationId1, stationId2, 5));
        lineService.addPathToLine(lineId, new PathRequest(stationId2, stationId3, 5));

        //when, then
        RestAssured.given()
                .get("/paths/start/{start-station-id}/end/{end-station-id}", stationId1, stationId3)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("totalDistance", equalTo(10));
    }
}
