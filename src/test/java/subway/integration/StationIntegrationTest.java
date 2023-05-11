package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.exception.ErrorCode;

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철역을 생성한다.")
    void createStation() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // expected
        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value())
            .header("Location", "/stations/1");
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("빈 이름으로 지하철 역을 생성한다")
    void createStation_empty_name() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "");

        // when
        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errorCode", equalTo(ErrorCode.INVALID_REQUEST.name()))
            .body("errorMessage[0]", equalTo("역 이름을 입력해 주세요."));
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    void createStationWithDuplicateName() {
        // given
        final Map<String, String> stationRequest = new HashMap<>();
        stationRequest.put("name", "강남역");
        saveStation(stationRequest);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then()
            .log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철역 목록을 조회한다.")
    void getStations() {
        /// given
        final Map<String, String> stationRequest1 = new HashMap<>();
        stationRequest1.put("name", "강남역");
        saveStation(stationRequest1);

        final Map<String, String> stationRequest2 = new HashMap<>();
        stationRequest2.put("name", "역삼역");
        saveStation(stationRequest2);

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
    @Sql("classpath:/init.sql")
    @DisplayName("지하철역을 조회한다.")
    void getStation() {
        /// given
        final Map<String, String> stationRequest = new HashMap<>();
        stationRequest.put("name", "강남역");
        saveStation(stationRequest);

        // expected
        RestAssured.given().log().all()
            .when()
            .get("/stations/{stationId}", 1)
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("name", is("강남역"));
    }

    @Test
    @Sql("classpath:/init.sql")
    @DisplayName("지하철역을 수정한다.")
    void updateStation() {
        // given
        final Map<String, String> stationRequest = new HashMap<>();
        stationRequest.put("name", "강남역");
        saveStation(stationRequest);

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
    @Sql("classpath:/init.sql")
    @DisplayName("지하철역을 제거한다.")
    void deleteStation() {
        // given
        final Map<String, String> stationRequest = new HashMap<>();
        stationRequest.put("name", "강남역");
        saveStation(stationRequest);

        // expected
        RestAssured.given().log().all()
            .when()
            .delete("/stations/1")
            .then().log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .extract();
    }

    private void saveStation(final Map<String, String> stationRequest) {
        RestAssured.given().log().all()
            .body(stationRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all();
    }
}
