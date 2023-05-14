package subway.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.AddInitStationToLineRequest;
import subway.controller.dto.AddStationLocation;
import subway.controller.dto.AddStationToLineRequest;
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.StationResponse;

public class LineStationIntegrationTest extends IntegrationTest {

    LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");

    @Test
    @DisplayName("노선에 초기 지하철을 추가한다.")
    void testAddInitStationToLine() {
        //given
        final ExtractableResponse<Response> createLineResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest1)
            .when().post("/lines")
            .then().log().all().
            extract();
        final LineResponse lineResponse = createLineResponse.as(LineResponse.class);

        final Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        final ExtractableResponse<Response> createStationResponse1 = given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse1 = createStationResponse1.as(StationResponse.class);

        final Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신논현역");
        final ExtractableResponse<Response> createStationResponse2 = given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse2 = createStationResponse2.as(StationResponse.class);

        //when
        final AddInitStationToLineRequest request = new AddInitStationToLineRequest(
            lineResponse.getName(), stationResponse1.getName(), stationResponse2.getName(),
            10L);
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선의 맨 위에 지하철을 추가한다.")
    void testAddStationToLineOnTop() {
        //given
        final ExtractableResponse<Response> createLineResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest1)
            .when().post("/lines")
            .then().log().all().
            extract();
        final LineResponse lineResponse = createLineResponse.as(LineResponse.class);

        final Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        final ExtractableResponse<Response> createStationResponse1 = given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse1 = createStationResponse1.as(StationResponse.class);

        final Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신논현역");
        final ExtractableResponse<Response> createStationResponse2 = given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse2 = createStationResponse2.as(StationResponse.class);

        final Map<String, String> params3 = new HashMap<>();
        params3.put("name", "신사역");
        final ExtractableResponse<Response> createStationResponse3 = given().log().all()
            .body(params3)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse3 = createStationResponse3.as(StationResponse.class);

        final AddInitStationToLineRequest initRequest = new AddInitStationToLineRequest(
            lineResponse.getName(), stationResponse1.getName(), stationResponse2.getName(),
            10L);
        given().log().all()
            .body(initRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();

        final AddStationToLineRequest request = new AddStationToLineRequest(AddStationLocation.TOP,
            lineResponse.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 10L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/add")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선의 맨 아래에 지하철을 추가한다.")
    void testAddStationToLineOnBottom() {
        //given
        final ExtractableResponse<Response> createLineResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest1)
            .when().post("/lines")
            .then().log().all().
            extract();
        final LineResponse lineResponse = createLineResponse.as(LineResponse.class);

        final Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        final ExtractableResponse<Response> createStationResponse1 = given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse1 = createStationResponse1.as(StationResponse.class);

        final Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신논현역");
        final ExtractableResponse<Response> createStationResponse2 = given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse2 = createStationResponse2.as(StationResponse.class);

        final Map<String, String> params3 = new HashMap<>();
        params3.put("name", "신사역");
        final ExtractableResponse<Response> createStationResponse3 = given().log().all()
            .body(params3)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse3 = createStationResponse3.as(StationResponse.class);

        final AddInitStationToLineRequest initRequest = new AddInitStationToLineRequest(
            lineResponse.getName(), stationResponse1.getName(), stationResponse2.getName(),
            10L);
        given().log().all()
            .body(initRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();

        final AddStationToLineRequest request = new AddStationToLineRequest(AddStationLocation.BOTTOM,
            lineResponse.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 10L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/add")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("노선의 사이에 지하철을 추가한다.")
    void testAddStationToLineOnBetween() {
        //given
        final ExtractableResponse<Response> createLineResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest1)
            .when().post("/lines")
            .then().log().all().
            extract();
        final LineResponse lineResponse = createLineResponse.as(LineResponse.class);

        final Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        final ExtractableResponse<Response> createStationResponse1 = given().log().all()
            .body(params1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse1 = createStationResponse1.as(StationResponse.class);

        final Map<String, String> params2 = new HashMap<>();
        params2.put("name", "신논현역");
        final ExtractableResponse<Response> createStationResponse2 = given().log().all()
            .body(params2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse2 = createStationResponse2.as(StationResponse.class);

        final Map<String, String> params3 = new HashMap<>();
        params3.put("name", "신사역");
        final ExtractableResponse<Response> createStationResponse3 = given().log().all()
            .body(params3)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        final StationResponse stationResponse3 = createStationResponse3.as(StationResponse.class);

        final AddInitStationToLineRequest initRequest = new AddInitStationToLineRequest(
            lineResponse.getName(), stationResponse1.getName(), stationResponse2.getName(),
            10L);
        given().log().all()
            .body(initRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();

        final AddStationToLineRequest request = new AddStationToLineRequest(AddStationLocation.BETWEEN,
            lineResponse.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 10L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/add")
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
