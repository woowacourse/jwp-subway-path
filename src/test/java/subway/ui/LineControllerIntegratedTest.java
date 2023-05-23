package subway.ui;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import subway.business.service.LineService;
import subway.business.service.dto.LineResponse;
import subway.business.service.dto.LineSaveRequest;
import subway.business.service.dto.StationAddToLineRequest;
import subway.ui.dto.StationDeleteRequest;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineControllerIntegratedTest {
    @Autowired
    private LineService lineService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM line");
    }

    @DisplayName("노선을 생성한다.")
    @Test
    void shouldCreateLineWhenRequest() {
        LineSaveRequest lineSaveRequest = new LineSaveRequest(
                "2호선",
                "잠실역",
                "몽촌토성역",
                5
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(lineSaveRequest)
                .when().post("/lines")
                .then().log().all()
                .body("name", is("2호선"))
                .body("sections[0].upwardStation.name", is("잠실역"))
                .body("sections[0].downwardStation.name", is("몽촌토성역"))
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 역을 추가한다.")
    @Test
    void shouldAddStationToLineWhenRequest() {
        LineSaveRequest lineSaveRequest = new LineSaveRequest(
                "2호선",
                "잠실역",
                "몽촌토성역",
                5
        );
        LineResponse lineResponse = lineService.createLine(lineSaveRequest);

        StationAddToLineRequest stationAddToLineRequest = new StationAddToLineRequest(
                "강남역",
                "몽촌토성역",
                "상행",
                3
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(stationAddToLineRequest)
                .when().post("/lines/" + lineResponse.getId() + "/stations")
                .then().log().all()
                .body("name", is("2호선"))
                .body("sections[0].upwardStation.name", is("잠실역"))
                .body("sections[0].downwardStation.name", is("강남역"))
                .body("sections[1].upwardStation.name", is("강남역"))
                .body("sections[1].downwardStation.name", is("몽촌토성역"))
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("노선에 포함된 역 한 개를 제외한다.")
    @Test
    void shouldRemoveStationFromLineWhenRequest() {
        LineSaveRequest lineSaveRequest = new LineSaveRequest(
                "2호선",
                "잠실역",
                "몽촌토성역",
                5
        );
        lineService.createLine(lineSaveRequest);

        StationAddToLineRequest stationAddToLineRequest = new StationAddToLineRequest(
                "강남역",
                "몽촌토성역",
                "상행",
                3
        );
        lineService.addStationToLine(1L, stationAddToLineRequest);

        StationDeleteRequest stationDeleteRequest = new StationDeleteRequest("강남역");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(stationDeleteRequest)
                .when().delete("/lines/1/stations")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @DisplayName("특정 노선의 이름과 포함된 모든 역을 조회한다.")
    @Test
    void shouldReturnLineAndStationsWhenRequest() {
        LineSaveRequest lineSaveRequest = new LineSaveRequest(
                "2호선",
                "잠실역",
                "몽촌토성역",
                5
        );
        LineResponse lineResponse = lineService.createLine(lineSaveRequest);

        RestAssured.given().log().all()
                .when().get("/lines/" + lineResponse.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", is("2호선"))
                .body("sections[0].upwardStation.name", is("잠실역"))
                .body("sections[0].downwardStation.name", is("몽촌토성역"));
    }

    @DisplayName("모든 노선의 이름과 포함된 모든 역을 조회한다.")
    @Test
    void shouldReturnAllLinesAndStationsWhenRequest() {
        LineSaveRequest lineSaveRequest1 = new LineSaveRequest(
                "2호선",
                "잠실역",
                "몽촌토성역",
                5
        );
        lineService.createLine(lineSaveRequest1);

        LineSaveRequest lineSaveRequest2 = new LineSaveRequest(
                "3호선",
                "매봉역",
                "교대역",
                5
        );
        lineService.createLine(lineSaveRequest2);

        RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("[0].name", is("2호선"))
                .body("[0].sections[0].upwardStation.name", is("잠실역"))
                .body("[0].sections[0].downwardStation.name", is("몽촌토성역"))
                .body("[1].name", is("3호선"))
                .body("[1].sections[0].upwardStation.name", is("매봉역"))
                .body("[1].sections[0].downwardStation.name", is("교대역"));
    }
}
