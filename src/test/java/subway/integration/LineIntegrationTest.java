package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.application.request.LineRequest;
import subway.application.request.SectionRequest;
import subway.application.request.StationRequest;
import subway.application.response.LineResponse;
import subway.application.response.StationResponse;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.IntegrationFixture.*;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선");
        lineRequest2 = new LineRequest("구신분당선");
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
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

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
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
    void getLines() throws JsonProcessingException {
        // given
        final SectionRequest request = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), 10);
        final String json = jsonSerialize(request);
        final Long lineId = 1L;

        given().body(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/register", lineId)
                .then().statusCode(HttpStatus.CREATED.value());

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        final String string = response.asString();
        final LineResponse[] responses = OBJECT_MAPPER.readValue(string, LineResponse[].class);
        assertAll(
                () -> assertThat(responses)
                        .extracting(LineResponse::getName)
                        .contains(LINE1.getName().getValue(), LINE2.getName().getValue())
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() throws JsonProcessingException {
        // given
        final SectionRequest request = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), 10);
        final String json = jsonSerialize(request);
        final Long lineId = 1L;

        given().body(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/register", lineId)
                .then().statusCode(HttpStatus.CREATED.value());

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final LineResponse result = response.as(LineResponse.class);
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(LINE1.getName().getValue()),
                () -> assertThat(result.getStations())
                        .extracting(StationResponse::getName)
                        .containsExactly(STATION_A.getName().getValue(), STATION_B.getName().getValue())
        );
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        final Long lineId = 1L;
        final ExtractableResponse<Response> response = RestAssured
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
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에 역을 최초 등록한다.")
    @Test
    void addInitialStationInLine() throws JsonProcessingException {
        final SectionRequest request = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), 10);

        final String json = jsonSerialize(request);

        given().body(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/1/register")
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void deleteStationInLine() throws JsonProcessingException {
        final SectionRequest request = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), 10);
        final String json = jsonSerialize(request);
        given().body(json)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/1/register")
                .then().statusCode(HttpStatus.CREATED.value());
        final StationRequest stationRequest = new StationRequest(STATION_A.getName().getValue());

        given().body(jsonSerialize(stationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/1/unregister")
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}
