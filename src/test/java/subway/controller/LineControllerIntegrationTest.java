package subway.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
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
import subway.dto.line.LineEditRequest;
import subway.dto.line.LinesResponse;
import subway.dto.section.SectionCreateRequest;
import subway.dto.station.StationCreateRequest;
import subway.service.LineService;
import subway.service.SectionService;
import subway.service.StationService;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class LineControllerIntegrationTest {

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @Autowired
    private SectionService sectionService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("노선을 생성한다.")
    void create_line_success() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        long lineId = 1L;

        // when & then
        Response result = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines");

        result.
                then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/lines/" + lineId);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void find_all_lines_success() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineCreateRequest);

        // when & then
        Response response = given()
                .when().get("/lines");

        response
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("lines[0].lineId", equalTo(1))
                .body("lines[0].lineNumber", equalTo(2))
                .body("lines[0].name", equalTo("2호선"))
                .body("lines[0].color", equalTo("초록색"));
    }

    @Test
    @DisplayName("노선을 조회한다.")
    void find_line_success() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineCreateRequest);

        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
        StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
        stationService.saveStation(stationCreateRequest);
        stationService.saveStation(stationCreateRequest2);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3L);
        sectionService.insertSection(sectionCreateRequest);

        long lineId = lineCreateRequest.getLineNumber();

        // when & then
        Response response = given()
                .when().get("/lines/" + lineId);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("stations[0].id", equalTo(1))
                .body("stations[0].name", equalTo("잠실역"))
                .body("stations[1].id", equalTo(2))
                .body("stations[1].name", equalTo("잠실새내역"));
    }

    @Test
    @DisplayName("노선을 수정한다.")
    void edit_line_success() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineCreateRequest);

        LineEditRequest lineEditRequest = new LineEditRequest("8호선", 2L, "초록색");

        // when & then
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineEditRequest)
                .when().patch("/lines/1");

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void delete_line_success() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineCreateRequest);

        // when & then
        Response response = given()
                .when().delete("/lines/1");

        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        LinesResponse lineEntities = lineService.findAll();
        assertThat(lineEntities.getLines().size()).isEqualTo(0);
    }

}
