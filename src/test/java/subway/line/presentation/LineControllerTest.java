package subway.line.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.line.dto.LineCreateDto;
import subway.line.service.LineService;
import subway.section.dto.SectionCreateDto;
import subway.station.dto.StationCreateDto;
import subway.station.service.StationService;

@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineControllerTest {

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
  void getLines() {
    final Long line1 = lineService.createLine(new LineCreateDto("1호선"));
    final Long line2 = lineService.createLine(new LineCreateDto("2호선"));
    final Long 잠실새내역_id = stationService.create(new StationCreateDto("잠실새내역"));
    final Long 잠실역_id = stationService.create(new StationCreateDto("잠실역"));
    final Long 잠실나루역_id = stationService.create(new StationCreateDto("잠실나루역"));
    lineService.addSection(new SectionCreateDto(line1, 잠실역_id, 잠실나루역_id, 5));
    lineService.addSection(new SectionCreateDto(line2, 잠실새내역_id, 잠실역_id, 4));

    final ExtractableResponse<Response> extract = RestAssured
        .given()
        .when()
        .get("/lines")
        .then()
        .statusCode(HttpStatus.OK.value()).log().all()
        .extract();

    assertAll(
        () -> assertThat(extract.jsonPath().getString("name[0]")).isEqualTo("1호선"),
        () -> assertThat(extract.jsonPath().getList("stationResponseDtos[0]").size()).isEqualTo(2),
        () -> assertThat(extract.jsonPath().getString("name[1]")).isEqualTo("2호선"),
        () -> assertThat(extract.jsonPath().getList("stationResponseDtos[1]").size()).isEqualTo(2)
    );
  }

  @Test
  void getStations() {
    final Long lineId = lineService.createLine(new LineCreateDto("2호선"));
    final Long 잠실역_id = stationService.create(new StationCreateDto("잠실역"));
    final Long 잠실나루역_id = stationService.create(new StationCreateDto("잠실나루역"));
    lineService.addSection(new SectionCreateDto(lineId, 잠실역_id, 잠실나루역_id, 5));

    final ExtractableResponse<Response> extract = RestAssured
        .given()
        .when()
        .get("/lines/" + 1L)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract();

    assertThat(extract.jsonPath().getString("name[0]")).isEqualTo("잠실역");
    assertThat(extract.jsonPath().getString("name[1]")).isEqualTo("잠실나루역");
  }

  @Test
  void create() {
    final LineCreateDto lineCreateDto = new LineCreateDto("2호선");

    RestAssured
        .given()
          .body(lineCreateDto)
          .contentType(ContentType.JSON)
        .when()
          .post("/lines")
        .then()
          .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  void enrollStation() {
    final Long lineId = lineService.createLine(new LineCreateDto("2호선"));
    final Long 잠실역_id = stationService.create(new StationCreateDto("잠실역"));
    final Long 잠실나루역_id = stationService.create(new StationCreateDto("잠실나루역"));

    final SectionCreateDto sectionCreateDto = new SectionCreateDto(lineId, 잠실역_id, 잠실나루역_id, 5);

    RestAssured
        .given()
          .body(sectionCreateDto)
          .contentType(ContentType.JSON)
        .when()
          .post("/lines/stations")
        .then()
          .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  void disEnrollStation() {
    final Long lineId = lineService.createLine(new LineCreateDto("2호선"));
    final Long 잠실역_id = stationService.create(new StationCreateDto("잠실역"));
    final Long 잠실나루역_id = stationService.create(new StationCreateDto("잠실나루역"));
    lineService.addSection(new SectionCreateDto(lineId, 잠실역_id, 잠실나루역_id, 5));

    RestAssured
        .given()
        .when()
          .delete("/lines/" + lineId +"/stations/" + 잠실역_id)
        .then()
          .statusCode(HttpStatus.NO_CONTENT.value());
  }
}
