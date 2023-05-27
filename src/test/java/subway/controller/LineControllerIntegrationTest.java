package subway.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.line.LineRequest;
import subway.dto.line.LineResponse;
import subway.dto.section.SectionCreateRequest;
import subway.dto.station.StationRequest;
import subway.service.LineService;
import subway.service.SectionService;
import subway.service.StationService;

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
        LineRequest lineRequest = new LineRequest("2호선", 2L, "초록색");
        Long lineId = 1L;

        // when & then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/lines/" + lineId);
    }

    @Test
    @DisplayName("모든 노선을 조회한다.")
    void find_all_lines_success() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineRequest);

        // when & then
        RestAssured
                .given()
                .when().get("/lines")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", equalTo(1))
                .body("[0].lineNumber", equalTo(2))
                .body("[0].name", equalTo("2호선"))
                .body("[0].color", equalTo("초록색"));
    }

    @Test
    @DisplayName("노선을 조회한다.")
    void find_line_success() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineRequest);

        StationRequest stationRequest = new StationRequest("잠실역");
        StationRequest stationRequest2 = new StationRequest("잠실새내역");
        stationService.saveStation(stationRequest);
        stationService.saveStation(stationRequest2);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(2L, "잠실역", "잠실새내역", 3L);
        sectionService.addSection(sectionCreateRequest);

        Long lineId = lineRequest.getLineNumber();

        // when & then
        RestAssured
                .given()
                .when().get("/lines/" + lineId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("stations[0].id", equalTo(1))
                .body("stations[0].name", equalTo("잠실역"))
                .body("stations[1].id", equalTo(2))
                .body("stations[1].name", equalTo("잠실새내역"));
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void delete_line_success() {
        // given
        LineRequest lineRequest = new LineRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineRequest);

        // when & then
        RestAssured
                .given()
                .when().delete("/lines/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        final List<LineResponse> lineResponses = lineService.findAll();
        assertThat(lineResponses.size()).isEqualTo(0);
    }
}
