package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.dto.request.ConnectionEndpointRequest;
import subway.dto.request.ConnectionInitRequest;
import subway.dto.request.LineRequest;
import subway.dto.response.PathResponse;
import subway.ui.EndpointType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PathIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // 신분당선 : 강남역 -3- 잠실역 -2- 건대입구역 -1- 선릉역
        // 구신분당선 : 강남역 -2- 구의역 -1- 선릉역
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

        final ConnectionInitRequest connectionInitRequest = new ConnectionInitRequest(2L, 10);
        RestAssured.given()
                .body(connectionInitRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/1/init");

        final ConnectionEndpointRequest connectionDownRequest1 = new ConnectionEndpointRequest(EndpointType.DOWN, 12);
        RestAssured.given()
                .body(connectionDownRequest1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/3/endpoint");

        final ConnectionEndpointRequest connectionDownRequest2 = new ConnectionEndpointRequest(EndpointType.DOWN, 12);
        RestAssured.given()
                .body(connectionDownRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/1/stations/4/endpoint");

        final ConnectionInitRequest connectionInitRequest2 = new ConnectionInitRequest(5L, 12);
        RestAssured.given()
                .body(connectionInitRequest2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/2/stations/1/init");

        final ConnectionEndpointRequest connectionDownRequest3 = new ConnectionEndpointRequest(EndpointType.DOWN, 11);
        RestAssured.given()
                .body(connectionDownRequest3)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/2/stations/4/endpoint");
    }

    @DisplayName("최단 경로를 조회하고 거리와 요금을 계산한다")
    @Test
    void findShortestPath() {
        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/path?source=1&target=4")
                .then().log().all()
                .extract();

        // then
        final PathResponse pathResponse = response.body().as(PathResponse.class);
        final List<String> stations = pathResponse.getStations();
        final int distance = pathResponse.getDistance();
        final int fare = pathResponse.getFare();

        assertAll(
                () -> Assertions.assertThat(stations).containsExactly("강남역", "구의역", "선릉역"),
                () -> assertThat(distance).isEqualTo(23),
                () -> assertThat(fare).isEqualTo(1550)
        );
    }
}
