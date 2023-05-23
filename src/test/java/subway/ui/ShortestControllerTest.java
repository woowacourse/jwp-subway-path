package subway.ui;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.application.LineService;
import subway.application.StationService;
import subway.dto.LineRequest;
import subway.dto.PathRequest;
import subway.dto.StationRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@Sql(scripts = "classpath:truncate.sql", executionPhase = AFTER_TEST_METHOD)
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

    @DisplayName("한 노선에서 최단 경로와 요금을 조회할 수 있다")
    @Test
    void getDijkstraShortestPath() {
        //given
        Long lineId = lineService.saveLine(new LineRequest("1호선", "red", 500)).getId();

        Long stationId1 = stationService.saveStation(new StationRequest("광안역")).getId();
        Long stationId2 = stationService.saveStation(new StationRequest("전포역")).getId();
        Long stationId3 = stationService.saveStation(new StationRequest("노포역")).getId();

        lineService.addPathToLine(lineId, new PathRequest(stationId1, stationId2, 5));
        lineService.addPathToLine(lineId, new PathRequest(stationId2, stationId3, 5));

        //when, then
        given()
                .queryParam("age", 10)
                .get("/paths/start/{start-station-id}/end/{end-station-id}", stationId1, stationId3)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("totalDistance", equalTo(10))
                .body("totalCost", equalTo(700));
    }

    /**
     * 광안 <-5-> 전포 <-5-> 노포
     * 광안 <-1-> 부산 <-1-> 해운대 <-1-> 노포
     * expect: 3, 1550원
     */
    @DisplayName("여러 경로 중 최단 경로와 요금을 조회할 수 있다")
    @Test
    void getDijkstraShortestPath2() {
        //given
        Long lineId1 = lineService.saveLine(new LineRequest("1호선", "red", 500)).getId();
        Long lineId2 = lineService.saveLine(new LineRequest("2호선", "blue", 300)).getId();

        Long source = stationService.saveStation(new StationRequest("광안역")).getId();
        Long destination = stationService.saveStation(new StationRequest("노포역")).getId();

        Long stationId2 = stationService.saveStation(new StationRequest("전포역")).getId();
        Long stationId3 = stationService.saveStation(new StationRequest("부산역")).getId();
        Long stationId4 = stationService.saveStation(new StationRequest("해운대역")).getId();

        lineService.addPathToLine(lineId1, new PathRequest(source, stationId2, 5));
        lineService.addPathToLine(lineId1, new PathRequest(stationId2, destination, 5));

        lineService.addPathToLine(lineId2, new PathRequest(source, stationId3, 1));
        lineService.addPathToLine(lineId2, new PathRequest(stationId3, stationId4, 1));
        lineService.addPathToLine(lineId2, new PathRequest(stationId4, destination, 1));

        //when, then
        given()
                .queryParam("age", 20)
                .get("/paths/start/{start-station-id}/end/{end-station-id}", source, destination)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("totalDistance", equalTo(3))
                .body("totalCost", equalTo(1550));
    }
}
