package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.*;
import subway.entity.LineEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("2호선", "bg-green-600");
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineRequest1);
        String lindId = parseUri(createResponse.header("Location"));

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .when()
                        .get("/lines/{id}", lindId)
                        .then()
                        .log().all()
                        .extract();
        // then
        LineStationResponse lineStationResponse = response.as(LineStationResponse.class);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
        assertThat(lineStationResponse.getLineResponse().getName()).isEqualTo("신분당선");
        assertThat(lineStationResponse.getLineResponse().getColor()).isEqualTo("bg-red-600");

    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void registerStation() {
        // given
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(lineRequest1);
        Long lineId1 = Long.parseLong(parseUri(lineResponse.header("Location")));

        StationRequest stationRequest1 = new StationRequest("강남역");
        StationRequest stationRequest2 = new StationRequest("판교역");
        ExtractableResponse<Response> stationResponse1 = StationIntegrationTest.지하철_역_생성_요청(stationRequest1);
        ExtractableResponse<Response> stationResponse2 = StationIntegrationTest.지하철_역_생성_요청(stationRequest2);
        Long stationId1 = Long.parseLong(parseUri(stationResponse1.header("Location")));
        Long stationId2 = Long.parseLong(parseUri(stationResponse2.header("Location")));


        // when
        LineStationRequest lineStationRequest = new LineStationRequest(stationId1, stationId2, 10);
        ExtractableResponse<Response> registerStationResponse = 지하철_노선에_역_등록_요청(lineId1, lineStationRequest);

        // then
        assertThat(registerStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(registerStationResponse.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_생성_요청(lineRequest1);

        // when
        ExtractableResponse<Response> response2 = 지하철_노선_생성_요청(lineRequest1);

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 모든 노선과 각 노선에 등록된 역을 조회한다.")
    @Test
    void findRegisteredStations() {
        // given
        ExtractableResponse<Response> lineResponse1 = 지하철_노선_생성_요청(lineRequest1);
        ExtractableResponse<Response> lineResponse2 = 지하철_노선_생성_요청(lineRequest2);

        Long lineId1 = Long.parseLong(parseUri(lineResponse1.header("Location")));
        Long lineId2 = Long.parseLong(parseUri(lineResponse2.header("Location")));

        StationRequest stationRequest1 = new StationRequest("강남역");
        StationRequest stationRequest2 = new StationRequest("판교역");
        StationRequest stationRequest3 = new StationRequest("잠실역");
        ExtractableResponse<Response> stationResponse1 = StationIntegrationTest.지하철_역_생성_요청(stationRequest1);
        ExtractableResponse<Response> stationResponse2 = StationIntegrationTest.지하철_역_생성_요청(stationRequest2);
        ExtractableResponse<Response> stationResponse3 = StationIntegrationTest.지하철_역_생성_요청(stationRequest3);
        Long stationId1 = Long.parseLong(parseUri(stationResponse1.header("Location")));
        Long stationId2 = Long.parseLong(parseUri(stationResponse2.header("Location")));
        Long stationId3 = Long.parseLong(parseUri(stationResponse3.header("Location")));

        LineStationRequest lineStationRequest1 = new LineStationRequest(stationId1, stationId2, 10);
        LineStationRequest lineStationRequest2 = new LineStationRequest(stationId1, stationId3, 5);
        ExtractableResponse<Response> registerStationResponse1 = 지하철_노선에_역_등록_요청(lineId1, lineStationRequest1);
        ExtractableResponse<Response> registerStationResponse2 = 지하철_노선에_역_등록_요청(lineId2, lineStationRequest2);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().
                extract();

        // then
        List<LineStationResponse> lineStationResponses = response.jsonPath().getList(".", LineStationResponse.class);
        LineStationResponse 신분당선_정보 = lineStationResponses.get(0);
        LineStationResponse 호선2_정보 = lineStationResponses.get(1);

        assertThat(신분당선_정보.getLineResponse())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(LineResponse.of(new LineEntity(lineRequest1.getName(), lineRequest1.getColor())));
        assertThat(신분당선_정보.getStationResponses()).extracting("id").containsExactly(1L, 2L);
        assertThat(신분당선_정보.getStationResponses()).extracting("name").containsExactly("강남역", "판교역");

        assertThat(호선2_정보.getLineResponse())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(LineResponse.of(new LineEntity(lineRequest2.getName(), lineRequest2.getColor())));
        assertThat(호선2_정보.getStationResponses()).extracting("id").containsExactly(1L, 3L);
        assertThat(호선2_정보.getStationResponses()).extracting("name").containsExactly("강남역", "잠실역");
    }

    @DisplayName("지하철 노선과 노선에 등록된 역을 조회한다.")
    @Test
    void findRegisteredStationsById() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineRequest1);
        Long lineId = Long.parseLong(parseUri(createResponse.header("Location")));

        StationRequest stationRequest1 = new StationRequest("동대구역");
        StationRequest stationRequest2 = new StationRequest("해운대역");
        ExtractableResponse<Response> createStationResponse1 = StationIntegrationTest.지하철_역_생성_요청(stationRequest1);
        ExtractableResponse<Response> createStationResponse2 = StationIntegrationTest.지하철_역_생성_요청(stationRequest2);
        Long upStationId = Long.parseLong(parseUri(createStationResponse1.header("Location")));
        Long downStationId = Long.parseLong(parseUri(createStationResponse2.header("Location")));

        LineStationRequest lineStationRequest1 = new LineStationRequest(upStationId, downStationId, 5);
        ExtractableResponse<Response> registerStationResponse = 지하철_노선에_역_등록_요청(lineId, lineStationRequest1);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", lineId)
                .then().log().all().
                extract();

        // then
        LineStationResponse lineStationResponse = response.as(LineStationResponse.class);

        assertThat(registerStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(registerStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        assertThat(lineStationResponse.getLineResponse())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(LineResponse.of(new LineEntity(lineRequest1.getName(), lineRequest1.getColor())));

        assertThat(lineStationResponse.getStationResponses())
                .extracting("id")
                .containsExactly(1L, 2L);

        assertThat(lineStationResponse.getStationResponses())
                .extracting("name")
                .containsExactly("동대구역", "해운대역");
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineRequest1);

        // when
        Long lineId = Long.parseLong(parseUri(createResponse.header("Location")));
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(lineRequest1);

        // when
        Long lineId = Long.parseLong(parseUri(createResponse.header("Location")));
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    protected static ExtractableResponse<Response> 지하철_노선_생성_요청(final LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
    }

    protected static ExtractableResponse<Response> 지하철_노선에_역_등록_요청(final Long lineId1, final LineStationRequest lineStationRequest1) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineStationRequest1)
                .when().post("/lines/{id}/stations", lineId1)
                .then().extract();
    }

    protected static String parseUri(String uri) {
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }
}
