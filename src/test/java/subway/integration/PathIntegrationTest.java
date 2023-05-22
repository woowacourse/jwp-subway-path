package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.request.ConnectionEndpointRequest;
import subway.dto.request.ConnectionInitRequest;
import subway.dto.response.PathResponse;
import subway.ui.EndpointType;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/dummy.sql")
public class PathIntegrationTest extends IntegrationTest {

    private static void patchEndpoint(final ConnectionEndpointRequest request, final String lineId, final String stationId) {
        RestAssured.given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/" + lineId + "/stations/" + stationId + "/endpoint");
    }

    private static void patchInit(final ConnectionInitRequest request, final String lineId, final String stationId) {
        RestAssured.given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("/lines/" + lineId + "/stations/" + stationId + "/init");
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 신분당선 : 강남역 -3- 잠실역 -2- 건대입구역 -1- 선릉역
        // 구신분당선 : 강남역 -2- 구의역 -1- 선릉역

        final ConnectionInitRequest connectionInitRequest = new ConnectionInitRequest(2L, 10);
        patchInit(connectionInitRequest, "1", "1");

        final ConnectionEndpointRequest connectionDownRequest1 = new ConnectionEndpointRequest(EndpointType.DOWN.getValue(), 12);
        patchEndpoint(connectionDownRequest1, "1", "3");

        final ConnectionEndpointRequest connectionDownRequest2 = new ConnectionEndpointRequest(EndpointType.DOWN.getValue(), 12);
        patchEndpoint(connectionDownRequest2, "1", "4");

        final ConnectionInitRequest connectionInitRequest2 = new ConnectionInitRequest(5L, 12);
        patchInit(connectionInitRequest2, "2", "1");

        final ConnectionEndpointRequest connectionDownRequest3 = new ConnectionEndpointRequest(EndpointType.DOWN.getValue(), 11);
        patchEndpoint(connectionDownRequest3, "2", "4");
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
