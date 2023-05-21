package subway.controller.route;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.line.LineCreateRequest;
import subway.dto.route.PathRequest;
import subway.dto.section.SectionCreateRequest;
import subway.dto.station.StationCreateRequest;
import subway.service.LineService;
import subway.service.SectionService;
import subway.service.StationService;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class RouteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @Autowired
    private SectionService sectionService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void find_shortest_route() {
        // given
        PathRequest req = new PathRequest("잠실역", "종합운동장역");
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineCreateRequest);

        StationCreateRequest stationCreateRequest1 = new StationCreateRequest("잠실역");
        StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
        StationCreateRequest stationCreateRequest3 = new StationCreateRequest("종합운동장역");
        stationService.saveStation(stationCreateRequest1);
        stationService.saveStation(stationCreateRequest2);
        stationService.saveStation(stationCreateRequest3);

        SectionCreateRequest sectionCreateRequest1 = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
        SectionCreateRequest sectionCreateRequest2 = new SectionCreateRequest("2호선", "잠실새내역", "종합운동장역", 5);
        sectionService.insertSection(sectionCreateRequest1);
        sectionService.insertSection(sectionCreateRequest2);

        // when & then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(req)
                .when().get("/routes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("paths[0].station.name", equalTo("잠실역"))
                .body("paths[0].lineNames[0]", equalTo("2호선"))
                .body("paths[1].station.name", equalTo("잠실새내역"))
                .body("paths[1].lineNames[0]", equalTo("2호선"))
                .body("paths[2].station.name", equalTo("종합운동장역"))
                .body("paths[2].lineNames[0]", equalTo("2호선"))
                .body("fee", equalTo(1250));
    }
}
