package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.StationsSavingRequest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("특정 노선에서의 역 추가 및 삭제 테스트")
public class SectionIntegrationTest extends IntegrationTest {
    @DisplayName("지하철 노선에 처음 두 역을 등록할 수 있다.")
    @Test
    void initialize() {
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        String requestUri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .header("location");

        RestAssured.given().log().all()
                .body(Map.of("name", "개룡역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        RestAssured.given().log().all()
                .body(Map.of("name", "거여역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        StationsSavingRequest stationsSavingRequest
                = new StationsSavingRequest("개룡역", "거여역", 5, true);
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationsSavingRequest)
                .when().post(requestUri + "/stations")
                .then().log().all()
                .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response2.header("Location")).isNotBlank();
    }

    @DisplayName("지하철 노선에 상행 방향으로 역을 추가할 수 있다.")
    @Test
    void insertUp() {
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        String requestUri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .header("location");

        RestAssured.given().log().all()
                .body(Map.of("name", "개룡역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        RestAssured.given().log().all()
                .body(Map.of("name", "거여역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        StationsSavingRequest stationsSavingRequest
                = new StationsSavingRequest("개룡역", "거여역", 5, false);
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationsSavingRequest)
                .when().post(requestUri + "/stations")
                .then().log().all()
                .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response2.header("Location")).isNotBlank();
    }

    @DisplayName("역과 역 사이의 거리로 양의 정수만을 입력할 수 있다.")
    @Test
    void invalidDistance() {
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        String requestUri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .header("location");

        RestAssured.given().log().all()
                .body(Map.of("name", "개룡역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        RestAssured.given().log().all()
                .body(Map.of("name", "거여역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        String json = "{\n" +
                "    \"previousStationName\": \"개룡역\",\n" +
                "    \"nextStationName\": \"거여역\",\n" +
                "    \"distance\": -3,\n" +
                "    \"down\": false\n" +
                "}";
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json)
                .when().post(requestUri + "/stations")
                .then().log().all()
                .extract();

        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("역과 역 사이에 추가하는 과정에서 거리가 양의 정수보다 내려갈 수 없다.")
    @Test
    void invalidDistance2() {
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        String requestUri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .header("location");

        RestAssured.given().log().all()
                .body(Map.of("name", "개룡역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        RestAssured.given().log().all()
                .body(Map.of("name", "거여역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        RestAssured.given().log().all()
                .body(Map.of("name", "용암역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        StationsSavingRequest stationsSavingRequest
                = new StationsSavingRequest("개룡역", "거여역", 5, false);
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationsSavingRequest)
                .when().post(requestUri + "/stations")
                .then().log().all()
                .extract();

        StationsSavingRequest stationsSavingRequest2
                = new StationsSavingRequest("개룡역", "용암역", 5, false);
        ExtractableResponse<Response> response3 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationsSavingRequest2)
                .when().post(requestUri + "/stations")
                .then().log().all()
                .extract();

        assertThat(response3.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선상에 있는 역을 삭제할 수 있습니다.")
    void delete() {
        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        String requestUri = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .header("location");

        RestAssured.given().log().all()
                .body(Map.of("name", "개룡역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        RestAssured.given().log().all()
                .body(Map.of("name", "거여역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        RestAssured.given().log().all()
                .body(Map.of("name", "용암역"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all();

        StationsSavingRequest stationsSavingRequest
                = new StationsSavingRequest("개룡역", "거여역", 5, false);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationsSavingRequest)
                .when().post(requestUri + "/stations")
                .then().log().all();

        StationsSavingRequest stationsSavingRequest2
                = new StationsSavingRequest("개룡역", "용암역", 2, false);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationsSavingRequest2)
                .when().post(requestUri + "/stations")
                .then().log().all();


        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(Map.of("name", "용암역"))
                .when().delete(requestUri + "/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
