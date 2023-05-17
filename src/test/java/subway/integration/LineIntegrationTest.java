package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql({"/test-data.sql"})
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
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        String lindId = parseUri(createResponse.header("Location"));
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .when()
                        .get("/lines/{id}", lindId)
                        .then()
                        .log().all()
                        .extract();
        // then
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
        assertThat(lineResponse.getName()).isEqualTo("신분당선");

    }

    @DisplayName("지하철 노선에 역을 등록한다.")
    @Test
    void registerStation() {
        // given
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().extract();

        Long lineId1 = Long.parseLong(parseUri(lineResponse.header("Location")));

        StationRequest stationRequest1 = new StationRequest("강남역");
        ExtractableResponse<Response> stationResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest1)
                .when().post("/stations")
                .then().extract();

        StationRequest stationRequest2 = new StationRequest("판교역");
        ExtractableResponse<Response> stationResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest2)
                .when().post("/stations")
                .then().extract();

        Long stationId1 = Long.parseLong(parseUri(stationResponse1.header("Location")));
        Long stationId2 = Long.parseLong(parseUri(stationResponse2.header("Location")));

        RegisterStationRequest registerStationRequest = new RegisterStationRequest(stationId1, stationId2, 10);

        // when
        ExtractableResponse<Response> registerStationResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest)
                .when().post("/lines/{id}/stations", lineId1)
                .then().extract();

        // then
        assertThat(registerStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(registerStationResponse.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given

        ExtractableResponse<Response> response1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 모든 노선을 조회한다.")
    @Test
    void findAllLines() {
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
                .map(it -> Long.parseLong(parseUri(it.header("Location"))))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLineById() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(parseUri(createResponse.header("Location")));
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultResponse.getId()).isEqualTo(lineId);
    }

    @DisplayName("지하철 모든 노선과 각 노선에 등록된 역을 조회한다.")
    @Test
    void findRegisteredStations() {
        // given
        ExtractableResponse<Response> lineResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().extract();

        ExtractableResponse<Response> lineResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().extract();

        Long lineId1 = Long.parseLong(parseUri(lineResponse1.header("Location")));
        Long lineId2 = Long.parseLong(parseUri(lineResponse2.header("Location")));

        StationRequest stationRequest1 = new StationRequest("강남역");
        ExtractableResponse<Response> stationResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest1)
                .when().post("/stations")
                .then().extract();

        StationRequest stationRequest2 = new StationRequest("판교역");
        ExtractableResponse<Response> stationResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest2)
                .when().post("/stations")
                .then().extract();

        StationRequest stationRequest3 = new StationRequest("잠실역");
        ExtractableResponse<Response> stationResponse3 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest3)
                .when().post("/stations")
                .then().extract();

        Long stationId1 = Long.parseLong(parseUri(stationResponse1.header("Location")));
        Long stationId2 = Long.parseLong(parseUri(stationResponse2.header("Location")));
        Long stationId3 = Long.parseLong(parseUri(stationResponse3.header("Location")));

        RegisterStationRequest registerStationRequest1 = new RegisterStationRequest(stationId1, stationId2, 10);
        RegisterStationRequest registerStationRequest2 = new RegisterStationRequest(stationId1, stationId3, 5);

        ExtractableResponse<Response> registerStationResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest1)
                .when().post("/lines/{id}/stations", lineId1)
                .then().extract();

        ExtractableResponse<Response> registerStationResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest2)
                .when().post("/lines/{id}/stations", lineId2)
                .then().extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/stations")
                .then().log().all().
                extract();

        // then
        List<LineStationResponse> lineStationResponses = response.jsonPath().getList(".", LineStationResponse.class);
        assertThat(lineStationResponses.get(0).getLineResponse())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new LineResponse(null, lineRequest1.getName(), lineRequest1.getColor()));
        assertThat(lineStationResponses.get(0).getStationResponses()).extracting("id").containsExactly(1L, 2L);
        assertThat(lineStationResponses.get(0).getStationResponses()).extracting("name").containsExactly("강남역", "판교역");

        assertThat(lineStationResponses.get(1).getLineResponse())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new LineResponse(null, lineRequest2.getName(), lineRequest2.getColor()));
        assertThat(lineStationResponses.get(1).getStationResponses()).extracting("id").containsExactly(1L, 3L);
        assertThat(lineStationResponses.get(1).getStationResponses()).extracting("name").containsExactly("강남역", "잠실역");
    }

    @DisplayName("지하철 노선과 노선에 등록된 역을 조회한다.")
    @Test
    void findRegisteredStationsById() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().extract();

        StationRequest stationRequest1 = new StationRequest("동대구역");
        StationRequest stationRequest2 = new StationRequest("해운대역");

        ExtractableResponse<Response> createStationResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest1)
                .when().post("/stations")
                .then().extract();

        ExtractableResponse<Response> createStationResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest2)
                .when().post("/stations")
                .then().extract();

        Long lineId = Long.parseLong(parseUri(createResponse.header("Location")));
        Long upStationId = Long.parseLong(parseUri(createStationResponse1.header("Location")));
        Long downStationId = Long.parseLong(parseUri(createStationResponse2.header("Location")));
        RegisterStationRequest registerStationRequest1 = new RegisterStationRequest(upStationId, downStationId, 5);
        ExtractableResponse<Response> registerStationResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(registerStationRequest1)
                .when().post("/lines/{id}/stations", lineId)
                .then().extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}/stations", lineId)
                .then().log().all().
                extract();

        // then
        LineStationResponse lineStationResponse = response.as(LineStationResponse.class);

        assertThat(registerStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(registerStationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        assertThat(lineStationResponse.getLineResponse())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new LineResponse(null, lineRequest1.getName(), lineRequest1.getColor()));

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
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

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
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

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

    private String parseUri(String uri) {
        String[] parts = uri.split("/");
        return parts[parts.length - 1];
    }
}
