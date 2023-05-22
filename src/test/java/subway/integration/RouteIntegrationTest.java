package subway.integration;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.dto.LineRequest;
import subway.application.dto.SectionRequest;
import subway.application.dto.StationRequest;

@DisplayName("경로 조회 기능")
public class RouteIntegrationTest extends IntegrationTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("주어진 출발역에서 도착역까지 가기 위한 최소 경로 및 최소 비용을 반환한다.")
    void getShortestRoute() {
        // given
        saveLine(new LineRequest("이호선", "green", 500));
        saveLine(new LineRequest("팔호선", "pink", 1000));

        saveStation(new StationRequest("잠실역"));
        saveStation(new StationRequest("선릉역"));
        saveStation(new StationRequest("강남역"));
        saveStation(new StationRequest("복정역"));
        saveStation(new StationRequest("남위례역"));
        saveStation(new StationRequest("신림역"));

        saveSection(new SectionRequest(1L, 1L, 2L, 10));
        saveSection(new SectionRequest(1L, 2L, 3L, 7));
        saveSection(new SectionRequest(1L, 3L, 4L, 5));
        saveSection(new SectionRequest(1L, 4L, 6L, 3));
        saveSection(new SectionRequest(2L, 2L, 5L, 8));
        saveSection(new SectionRequest(2L, 5L, 6L, 2));

        final Map<String, Integer> queryParams = new HashMap<>();
        queryParams.put("sourceStationId", 1);
        queryParams.put("targetStationId", 6);

        // expected
        RestAssured.given().log().all()
            .queryParams(queryParams)
            .when()
            .get("/routes")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("fare.normalFare", equalTo(2450))
            .body("fare.teenagerFare", equalTo(1680))
            .body("fare.childFare", equalTo(1050))
            .body("stations[0].id", equalTo(1))
            .body("stations[0].name", equalTo("잠실역"))
            .body("stations[1].id", equalTo(2))
            .body("stations[1].name", equalTo("선릉역"))
            .body("stations[2].id", equalTo(5))
            .body("stations[2].name", equalTo("남위례역"))
            .body("stations[3].id", equalTo(6))
            .body("stations[3].name", equalTo("신림역"));
    }

    private void saveLine(final LineRequest lineRequest) {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest)
            .when().post("/lines")
            .then().log().all();
    }

    private void saveStation(final StationRequest stationRequest) {
        RestAssured.given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all();
    }

    private void saveSection(final SectionRequest sectionRequest) {
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/sections")
            .then().log().all().extract();
    }
}
