package subway.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static subway.exception.ErrorCode.INVALID_REQUEST;
import static subway.exception.ErrorCode.STATION_NAME_DUPLICATED;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.dto.StationRequest;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("지하철역을 생성한다.")
    void createStation() {
        RestAssured.given().log().all()
            .body(new StationRequest("강남역"))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", "/stations/1");
    }

    @Test
    @DisplayName("빈 이름으로 지하철 역을 생성한다")
    void createStation_empty_name() {
        RestAssured.given().log().all()
            .body(new StationRequest(""))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(INVALID_REQUEST.name()))
            .body("errorMessage[0]", equalTo("역 이름을 입력해 주세요."));
    }

    @Test
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    void createStation_duplicate_name() {
        // given
        final StationRequest stationRequest = new StationRequest("강남역");
        saveStation(stationRequest);

        // expected
        RestAssured.given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then()
            .log().all()
            .body("errorCode", equalTo(STATION_NAME_DUPLICATED.name()))
            .body("errorMessage[0]", equalTo("역 이름은 중복될 수 없습니다"));
    }

    @Test
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        /// given
        saveStation(new StationRequest("강남역"));
        saveStation(new StationRequest("역삼역"));

        // expected
        RestAssured.given().log().all()
            .when()
            .get("/stations")
            .then().log().all()
            .body("size", Matchers.is(2))
            .body("[0].name", is("강남역"))
            .body("[1].name", is("역삼역"));
    }

    @Test
    @DisplayName("지하철역을 조회한다.")
    void getStationById() {
        /// given
        saveStation(new StationRequest("강남역"));

        // expected
        RestAssured.given().log().all()
            .when()
            .get("/stations/{stationId}", 1)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("name", is("강남역"));
    }

    @Test
    @DisplayName("지하철역을 수정한다.")
    void updateStation() {
        // given
        saveStation(new StationRequest("강남역"));

        // expected
        Map<String, String> otherParams = new HashMap<>();
        otherParams.put("name", "삼성역");

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(otherParams)
            .when()
            .put("/stations/1")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("지하철역을 제거한다.")
    void deleteStation() {
        // given
        saveStation(new StationRequest("강남역"));

        // expected
        RestAssured.given().log().all()
            .when()
            .delete("/stations/1")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

    private void saveStation(final StationRequest stationRequest) {
        RestAssured.given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all();
    }
}
