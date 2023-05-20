package subway.ui;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.business.service.LineService;
import subway.business.service.StationService;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.StationRequest;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PathControllerIntegratedTest {
    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM section");
        jdbcTemplate.update("DELETE FROM line");
        jdbcTemplate.update("DELETE FROM station");
    }

    @DisplayName("역과 역 사이의 최단 경로 정보를 조회한다.")
    @Test
    void shouldReturnShortestPathResponseWhenRequest() {
        long sourceStationId = stationService.saveStation(new StationRequest("강남역"));
        long destStationId = stationService.saveStation(new StationRequest("역삼역"));
        lineService.createLine(new LineSaveRequest("2호선", sourceStationId, destStationId, 10));

        RestAssured.given().log().all()
                .when().get("/path?sourceStationId=" + sourceStationId + "&destStationId=" + destStationId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("stationNamesOfShortestPath[0]", is("강남역"))
                .body("stationNamesOfShortestPath[1]", is("역삼역"))
                .body("totalDistance", is(10))
                .body("totalFare", is(1250));
    }
}
