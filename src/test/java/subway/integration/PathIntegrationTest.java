package subway.integration;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.controller.dto.AddStationLocation;
import subway.controller.dto.request.AddInitStationToLineRequest;
import subway.controller.dto.request.AddStationToLineRequest;
import subway.controller.dto.request.FindShortestPathRequest;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.response.FindShortestPathResponse;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.StationInformationResponse;
import subway.controller.dto.response.StationResponse;

public class PathIntegrationTest extends IntegrationTest {

    LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
    LineRequest lineRequest2 = new LineRequest("2호선", "bg-blue-600");

    @Test
    @DisplayName("최단 경로를 찾는다.")
    void testFindShortestPath() {
        // 라인1 생성
        final ExtractableResponse<Response> createLineResponse1 = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest1)
            .when().post("/lines")
            .then().log().all().
            extract();
        final LineResponse lineResponse1 = createLineResponse1.as(LineResponse.class);

        // 라인2 생성
        final ExtractableResponse<Response> createLineResponse2 = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest2)
            .when().post("/lines")
            .then().log().all().
            extract();
        final LineResponse lineResponse2 = createLineResponse2.as(LineResponse.class);

        // 강남역 생성
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

        // 신논현역 생성
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

        // 신사역 생성
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

        // 라인 1 초기 2개역 추가 - 신분당선 -> 강남역 거리 10
        final AddInitStationToLineRequest initRequest1 = new AddInitStationToLineRequest(
            lineResponse1.getName(), stationResponse1.getName(), stationResponse2.getName(),
            10L);
        given().log().all()
            .body(initRequest1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();

        // 라인 1 맨 끝에 신사역 추가 거리 10
        final AddStationToLineRequest addStationToLineRequest1 = new AddStationToLineRequest(AddStationLocation.BOTTOM,
            lineResponse1.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 10L);

        final ExtractableResponse<Response> addStationToLineResponse1 = given().log().all()
            .body(addStationToLineRequest1)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station")
            .then().log().all()
            .extract();

        // 라인 2 초기 2개역 추가 - 신분당선 -> 강남역 거리 1
        final AddInitStationToLineRequest initRequest2 = new AddInitStationToLineRequest(
            lineResponse2.getName(), stationResponse1.getName(), stationResponse2.getName(),
            1L);
        given().log().all()
            .body(initRequest2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station/init")
            .then().log().all()
            .extract();

        // 라인 2 맨 끝에 신사역 추가 거리 1
        final AddStationToLineRequest addStationToLineRequest2 = new AddStationToLineRequest(AddStationLocation.BOTTOM,
            lineResponse2.getName(), stationResponse3.getName(),
            stationResponse1.getName(), stationResponse2.getName(), 1L);

        final ExtractableResponse<Response> addStationToLineResponse2 = given().log().all()
            .body(addStationToLineRequest2)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/line/station")
            .then().log().all()
            .extract();

        final FindShortestPathRequest request = new FindShortestPathRequest(stationResponse1.getName(),
            stationResponse3.getName());

        final ExtractableResponse<Response> response = given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/shortest-path")
            .then().log().all()
            .extract();

        final FindShortestPathResponse findShortestPathResponse = response.as(FindShortestPathResponse.class);

        final StationInformationResponse stationInformationResponse1 = new StationInformationResponse(
            stationResponse1.getId(),
            stationResponse1.getName(), 6L,
            lineResponse2.getName(), lineResponse2.getColor());
        final StationInformationResponse stationInformationResponse2 = new StationInformationResponse(
            stationResponse2.getId(),
            stationResponse2.getName(), 6L,
            lineResponse2.getName(), lineResponse2.getColor());
        final StationInformationResponse stationInformationResponse3 = new StationInformationResponse(
            stationResponse3.getId(),
            stationResponse3.getName(), 6L,
            lineResponse2.getName(), lineResponse2.getColor());

        final List<StationInformationResponse> expectedStationInformationResponses = List.of(
            stationInformationResponse1,
            stationInformationResponse2, stationInformationResponse3);
        final FindShortestPathResponse expectedResponse = new FindShortestPathResponse(
            expectedStationInformationResponses, 2L, 1250L);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findShortestPathResponse.getTotalCost()).isEqualTo(expectedResponse.getTotalCost());
        assertThat(findShortestPathResponse.getTotalDistance()).isEqualTo(expectedResponse.getTotalDistance());
        
        final List<StationInformationResponse> stationInformations = findShortestPathResponse.getStationInformations();
        for (int i = 0; i < stationInformations.size(); i++) {
            final StationInformationResponse stationInformationResponse = stationInformations.get(i);
            final StationInformationResponse expectedStationInformationResponse = expectedStationInformationResponses.get(
                i);
            assertThat(stationInformationResponse).extracting("stationId", "stationName", "lineName", "lineColor")
                .containsExactly(
                    expectedStationInformationResponse.getStationId(),
                    expectedStationInformationResponse.getStationName(),
                    expectedStationInformationResponse.getLineName(),
                    expectedStationInformationResponse.getLineColor()
                );
        }
    }
}
