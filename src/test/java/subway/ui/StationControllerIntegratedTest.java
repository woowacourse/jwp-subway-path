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
import subway.business.service.StationService;
import subway.business.service.dto.StationRequest;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StationControllerIntegratedTest {
    @Autowired
    private StationService stationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM section");
        jdbcTemplate.update("DELETE FROM station");
    }

    @DisplayName("역을 생성한다.")
    @Test
    void shouldCreateStationWhenRequest() {
        StationRequest stationRequest = new StationRequest("강남역");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("특정 역에 대한 정보를 조회한다.")
    @Test
    void shouldReturnStationResponseWhenRequest() {
        StationRequest stationRequest = new StationRequest("강남역");
        long id = stationService.saveStation(stationRequest);

        RestAssured.given().log().all()
                .when().get("/stations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", is((int) id))
                .body("name", is("강남역"));
    }

    @DisplayName("모든 역에 대한 정보를 조회한다.")
    @Test
    void shouldReturnAllStationResponsesWhenRequest() {
        StationRequest stationRequest1 = new StationRequest("강남역");
        long id1 = stationService.saveStation(stationRequest1);
        StationRequest stationRequest2 = new StationRequest("역삼역");
        long id2 = stationService.saveStation(stationRequest2);

        RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("[0].id", is((int) id1))
                .body("[0].name", is("강남역"))
                .body("[1].id", is((int) id2))
                .body("[1].name", is("역삼역"));
    }

    @DisplayName("역을 수정한다.")
    @Test
    void shouldUpdateStationWhenRequest() {
        StationRequest stationRequest = new StationRequest("강남역");
        long id = stationService.saveStation(stationRequest);
        StationRequest stationRequestToUpdate = new StationRequest("역삼역");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(stationRequestToUpdate)
                .when().put("/stations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void shouldDeleteStationWhenRequest() {
        StationRequest stationRequest = new StationRequest("강남역");
        long id = stationService.saveStation(stationRequest);

        RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
