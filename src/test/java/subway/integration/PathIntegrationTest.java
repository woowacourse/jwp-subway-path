package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.InitStationsRequest;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.PathResponse;
import subway.dto.RegisterStationRequest;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("최단 경로 조회 관련 기능")
class PathIntegrationTest extends IntegrationTest{

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private StationRequest stationRequest1;
    private StationRequest stationRequest2;
    private StationRequest stationRequest3;
    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "#FFFFFF");
        lineRequest2 = new LineRequest("구신분당선", "#CCCCCC");
        stationRequest1 = new StationRequest("잠실역");
        stationRequest2 = new StationRequest("강남역");
        stationRequest3 = new StationRequest("삼성역");
    }

    @DisplayName("지하철 노선 최단 경로를 조회한다.")
    @Test
    void getShortestPath() {
        // given
        LineResponse lineResponse = LineIntegrationTest.createLine(lineRequest1);
        StationResponse stationResponse1 = LineIntegrationTest.createStation(stationRequest1);
        StationResponse stationResponse2 = LineIntegrationTest.createStation(stationRequest2);
        StationResponse stationResponse3 = LineIntegrationTest.createStation(stationRequest3);
        final int distance1 = 10;
        final int distance2 = 20;

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), distance1);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        RegisterStationRequest registerStationRequest = new RegisterStationRequest(stationResponse3.getId(), stationResponse2.getId(), "right", distance2);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest)
                .when().post("/lines/{id}/stations", lineResponse.getId())
                .then().log().all()
                .extract();
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("startStationId", stationResponse1.getId())
                .param("endStationId", stationResponse3.getId())
                .when().get("/paths")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStationResponses().size()).isEqualTo(3);
        assertThat(pathResponse.getDistance()).isEqualTo(distance1 + distance2);
    }

    @DisplayName("지하철 노선 환승 최단 경로를 조회한다.")
    @Test
    void getTransferShortestPath() {
        // given
        LineResponse lineResponse1 = LineIntegrationTest.createLine(lineRequest1);
        StationResponse stationResponse1 = LineIntegrationTest.createStation(stationRequest1);
        StationResponse stationResponse2 = LineIntegrationTest.createStation(stationRequest2);
        StationResponse stationResponse3 = LineIntegrationTest.createStation(stationRequest3);
        final int distance1 = 10;
        final int distance2 = 20;
        final int extraDistance = 987654321;

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), distance1);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse1.getId())
                .then().log().all()
                .extract();

        RegisterStationRequest registerStationRequest1 = new RegisterStationRequest(stationResponse3.getId(), stationResponse2.getId(), "right", extraDistance);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest1)
                .when().post("/lines/{id}/stations", lineResponse1.getId())
                .then().log().all()
                .extract();

        LineResponse lineResponse2 = LineIntegrationTest.createLine(lineRequest2);
        InitStationsRequest initStationsRequest2 = new InitStationsRequest(stationResponse2.getId(), stationResponse3.getId(), distance2);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest2)
                .when().post("/lines/{id}/init", lineResponse2.getId())
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("startStationId", stationResponse1.getId())
                .param("endStationId", stationResponse3.getId())
                .when().get("/paths")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStationResponses().size()).isEqualTo(3);
        assertThat(pathResponse.getDistance()).isEqualTo(distance1 + distance2);
    }

    @DisplayName("지하철 노선 최단 경로가 없는 경우 400에러를 반환한다.")
    @Test
    void notExistShortestPath() {
        // given
        LineResponse lineResponse = LineIntegrationTest.createLine(lineRequest1);
        StationResponse stationResponse1 = LineIntegrationTest.createStation(stationRequest1);
        StationResponse stationResponse2 = LineIntegrationTest.createStation(stationRequest2);
        StationResponse stationResponse3 = LineIntegrationTest.createStation(stationRequest3);
        final int distance1 = 10;

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), distance1);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("startStationId", stationResponse1.getId())
                .param("endStationId", stationResponse3.getId())
                .when().get("/paths")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
