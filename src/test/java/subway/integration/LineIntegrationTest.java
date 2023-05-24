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
import subway.dto.RegisterStationRequest;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;
    private StationRequest stationRequest1;
    private StationRequest stationRequest2;
    private StationRequest stationRequest3;
    private StationRequest stationRequest4;


    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "#FFFFFF");
        lineRequest2 = new LineRequest("구신분당선", "#CCCCCC");
        stationRequest1 = new StationRequest("잠실역");
        stationRequest2 = new StationRequest("강남역");
        stationRequest3 = new StationRequest("삼성역");
        stationRequest4 = new StationRequest("서울역");
    }

    public static LineResponse createLine(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .jsonPath()
                .getObject("", LineResponse.class);
    }

    public static StationResponse createStation(StationRequest stationRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().log().all()
                .extract()
                .jsonPath()
                .getObject("", StationResponse.class);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성하면 실패한다.")
    @Test
    void failCreateLineWithDuplicateName() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineId);
    }

    @DisplayName("조회할 노선이 없으면 실패한다.")
    @Test
    void failGetNotExistLine() {
        // given
        Long dummyId = 999L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", dummyId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
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

    @DisplayName("수정할 노선이 없는 경우 지하철 노선을 수정할 수 없다.")
    @Test
    void cantUpdateNotExistLine() {
        // given
        Long dummyId = 999L;

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{lineId}", dummyId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선에 초기 역 등록한다.")
    void initLine() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        StationResponse stationResponse1 = createStation(stationRequest1);
        StationResponse stationResponse2 = createStation(stationRequest2);

        // when
        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), 10);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("초기화된 노선은 초기화할 수 없다.")
    void cantInitInitializedLine() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        StationResponse stationResponse1 = createStation(stationRequest1);
        StationResponse stationResponse2 = createStation(stationRequest2);
        StationResponse stationResponse3 = createStation(stationRequest3);
        StationResponse stationResponse4 = createStation(stationRequest4);

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), 10);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        // when
        InitStationsRequest initStationsSecondRequest = new InitStationsRequest(stationResponse3.getId(), stationResponse4.getId(), 10);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsSecondRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선에 역을 등록한다.")
    void registerStation() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        StationResponse stationResponse1 = createStation(stationRequest1);
        StationResponse stationResponse2 = createStation(stationRequest2);
        StationResponse stationResponse3 = createStation(stationRequest3);

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), 10);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        // when
        RegisterStationRequest registerStationRequest = new RegisterStationRequest(stationResponse3.getId(), stationResponse2.getId(), "right", 10);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest)
                .when().post("/lines/{id}/stations", lineResponse.getId())
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("노선에 등록할 기준역이 없는 경우 추가할 수 없다.")
    void cantRegisterNotExistBaseStation() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        StationResponse stationResponse1 = createStation(stationRequest1);
        StationResponse stationResponse2 = createStation(stationRequest2);
        StationResponse stationResponse3 = createStation(stationRequest3);
        StationResponse stationResponse4 = createStation(stationRequest4);

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), 10);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        // when
        RegisterStationRequest registerStationRequest = new RegisterStationRequest(stationResponse3.getId(), stationResponse4.getId(), "right", 10);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest)
                .when().post("/lines/{id}/stations", lineResponse.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선에 등록할 추가역이 없는 경우 추가할 수 없다.")
    void cantRegisterNotExistStation() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        // when
        RegisterStationRequest registerStationRequest = new RegisterStationRequest(999L, 9999L, "right", 10);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest)
                .when().post("/lines/{id}/stations", lineResponse.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선에서 역을 제거한다.")
    void deleteStationInLine() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        StationResponse stationResponse1 = createStation(stationRequest1);
        StationResponse stationResponse2 = createStation(stationRequest2);
        StationResponse stationResponse3 = createStation(stationRequest3);

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), 10);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        RegisterStationRequest registerStationRequest = new RegisterStationRequest(stationResponse3.getId(), stationResponse2.getId(), "right", 10);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest)
                .when().post("/lines/{id}/stations", lineResponse.getId())
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", lineResponse.getId(), stationResponse1.getId())
                .then().log().all()
                .extract();

        // then
        LineResponse response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineResponse.getId())
                .then().log().all()
                .extract()
                .as(LineResponse.class);
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getStationResponses().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("노선에서 제거할 역이 없는 경우 실패한다.")
    void deleteStationEmptyLine() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", lineResponse.getId(), 999L)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("노선에 등록된 역이 두 개인 경우 하나를 제거하면 전부 제거된다.")
    void deleteLineHaveTwoStations() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        StationResponse stationResponse1 = createStation(stationRequest1);
        StationResponse stationResponse2 = createStation(stationRequest2);

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), 10);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initStationsRequest)
                .when().post("/lines/{id}/init", lineResponse.getId())
                .then().log().all()
                .extract();

        // when
        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{lineId}/stations/{stationId}", lineResponse.getId(), stationResponse1.getId())
                .then().log().all()
                .extract();

        // then
        LineResponse response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineResponse.getId())
                .then().log().all()
                .extract()
                .as(LineResponse.class);
        assertThat(extract.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(response.getStationResponses()).isEmpty();
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에 역이 등록된 경우 노선을 제거할 수 없다.")
    @Test
    void cantDeleteLineStationRegistered() {
        // given
        LineResponse lineResponse = createLine(lineRequest1);

        StationResponse stationResponse1 = createStation(stationRequest1);
        StationResponse stationResponse2 = createStation(stationRequest2);

        InitStationsRequest initStationsRequest = new InitStationsRequest(stationResponse1.getId(), stationResponse2.getId(), 10);
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
                .when().delete("/lines/{lineId}", lineResponse.getId())
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
