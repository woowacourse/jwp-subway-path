package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.request.ConnectionEndpointRequest;
import subway.dto.request.ConnectionInitRequest;
import subway.dto.request.ConnectionMidRequest;
import subway.dto.request.LineRequest;
import subway.dto.response.LineStationResponse;
import subway.dto.response.StationResponse;
import subway.ui.EndpointType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineStationIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 신분당선
        // 구신분당선
        // 1: 강남역
        // 2: 잠실역
        // 3: 건대입구역
        // 4: 선릉역
        // 5: 구의역
        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines");

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines");

        final Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");

        RestAssured.given()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations");

        final Map<String, String> params2 = new HashMap<>();
        params2.put("name", "잠실역");

        RestAssured.given()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations");

        final Map<String, String> params3 = new HashMap<>();
        params3.put("name", "건대입구역");

        RestAssured.given()
                .body(params3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations");

        final Map<String, String> params4 = new HashMap<>();
        params4.put("name", "선릉역");

        RestAssured.given()
                .body(params4)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations");

        final Map<String, String> params5 = new HashMap<>();
        params5.put("name", "구의역");

        RestAssured.given()
                .body(params5)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations");
    }

    @Test
    @DisplayName("초기 두 역을 등록한다")
    void init() {
        // when
        final ConnectionInitRequest request = new ConnectionInitRequest(2L, 10);

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("상행 종점에 역을 연결한다")
    void up() {
        // given
        final ConnectionInitRequest initRequest = new ConnectionInitRequest(2L, 10);

        RestAssured.given()
                .body(initRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init");

        // when
        final ConnectionEndpointRequest request = new ConnectionEndpointRequest(EndpointType.UP.getValue(), 12);

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/3/endpoint")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("하행 종점에 역을 연결한다")
    void down() {
        // given
        final ConnectionInitRequest initRequest = new ConnectionInitRequest(2L, 10);

        RestAssured.given()
                .body(initRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init");

        // when & then
        final ConnectionEndpointRequest request = new ConnectionEndpointRequest(EndpointType.DOWN.getValue(), 21);

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/4/endpoint")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("연결되어있는 역 중간에 역을 등록한다")
    void mid() {
        // given
        final ConnectionInitRequest initRequest = new ConnectionInitRequest(2L, 10);

        RestAssured.given()
                .body(initRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init");

        // when
        final ConnectionMidRequest request = new ConnectionMidRequest(1L, 6);

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/3/mid")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("라인에 등록된 역을 삭제한다")
    void delete() {
        // given
        final ConnectionInitRequest initRequest = new ConnectionInitRequest(2L, 10);

        RestAssured.given()
                .body(initRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init");

        // when
        final ExtractableResponse<Response> response = RestAssured.given()
                .when()
                .delete("/lines/1/stations/1")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("해당 라인에 등록된 역을 조회한다")
    void showStationsLineById() {
        // given
        final ConnectionInitRequest line1Request = new ConnectionInitRequest(2L, 10);

        RestAssured.given()
                .body(line1Request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init");

        final ConnectionInitRequest line2Request = new ConnectionInitRequest(4L, 6);

        RestAssured.given()
                .body(line2Request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/2/stations/3/init");

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/1/stations")
                .then().log().all()
                .extract();

        // then
        final LineStationResponse lineStationResponse = response.as(LineStationResponse.class);
        final List<String> stationNames = lineStationResponse.getStationResponses()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> Assertions.assertThat(stationNames).containsOnly("강남역", "잠실역"),
                () -> Assertions.assertThat(lineStationResponse.getDistances()).containsOnly(10)
        );
    }

    @DisplayName("모든 라인에 등록된 역들과 거리를 조회한다")
    @Test
    void showStations() {
        // given
        final ConnectionInitRequest line1Request = new ConnectionInitRequest(2L, 10);

        RestAssured.given()
                .body(line1Request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init");

        final ConnectionInitRequest line2Request = new ConnectionInitRequest(4L, 6);

        RestAssured.given()
                .body(line2Request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/2/stations/3/init");

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/stations")
                .then().log().all()
                .extract();

        // then
        final List<StationResponse> stationResponses = response.jsonPath().getList("stationResponses.flatten()", StationResponse.class);
        final List<String> stationNames = stationResponses.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        final List<String> distances = response.jsonPath().getList("distances.flatten()", String.class);
        System.out.println(stationNames);
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> Assertions.assertThat(stationNames).contains("강남역", "잠실역", "건대입구역", "선릉역"),
                () -> Assertions.assertThat(distances).contains("6", "10")
        );
    }
}
