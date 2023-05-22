package subway.line.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.line.dto.LineCreateDto;
import subway.line.dto.ShortestPathRequest;
import subway.line.service.LineService;
import subway.section.dto.SectionCreateDto;
import subway.station.dto.StationCreateDto;
import subway.station.service.StationService;

@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PathControllerTest {

  @Autowired
  private LineService lineService;

  @Autowired
  private StationService stationService;

  @LocalServerPort
  int port;

  @BeforeEach
  public void setUp() {
    RestAssured.port = port;
  }

  @Test
  void getShortestPath() {
    final Long lineId1 = lineService.createLine(new LineCreateDto("1호선"));
    final Long lineId2 = lineService.createLine(new LineCreateDto("2호선"));
    final Long lineId3 = lineService.createLine(new LineCreateDto("3호선"));
    final Long 잠실새내역_id = stationService.create(new StationCreateDto("잠실새내역"));
    final Long 잠실역_id = stationService.create(new StationCreateDto("잠실역"));
    final Long 잠실나루역_id = stationService.create(new StationCreateDto("잠실나루역"));
    lineService.addSection(new SectionCreateDto(lineId1, 잠실새내역_id, 잠실나루역_id, 5));
    lineService.addSection(new SectionCreateDto(lineId2, 잠실새내역_id, 잠실역_id, 4));
    lineService.addSection(new SectionCreateDto(lineId3, 잠실나루역_id, 잠실역_id, 100));
    final ShortestPathRequest shortestPathRequest = new ShortestPathRequest(잠실새내역_id, 잠실역_id);

    RestAssured
        .given()
        .body(shortestPathRequest)
        .contentType(ContentType.JSON)
        .when()
        .get("/path")
        .then()
        .statusCode(HttpStatus.OK.value());
  }
}
