package subway.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.FinalLineResponse;
import subway.dto.LineNodeRequests;
import subway.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.IntegrationFixture.*;

@DisplayName("지하철역 관련 기능")
@Sql("/clear.sql")
public class StationIntegrationTest extends IntegrationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String LINE_NAME_2 = "2";
    private static final String LINE_NAME_3 = "3";


    private static String jsonSerialize(final Object request) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(request);
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        // when
        final ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then().log().all()
                .extract();

        // when
        final ExtractableResponse<Response> response = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        /// given
        final Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        final ExtractableResponse<Response> createResponse1 = given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then().log().all()
                .extract();

        final Map<String, String> params2 = new HashMap<>();
        params2.put("name", "역삼역");
        final ExtractableResponse<Response> createResponse2 = given().log().all()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then().log().all()
                .extract();

        // when
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/lines/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<Long> expectedStationIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());
        final List<Long> resultStationIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        /// given
        final Map<String, String> params1 = new HashMap<>();
        params1.put("name", "강남역");
        final ExtractableResponse<Response> createResponse = given().log().all()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then().log().all()
                .extract();

        // when
        final Long stationId = Long.parseLong(createResponse.header("Location").split("/")[3]);
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/lines/stations/{stationId}", stationId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final StationResponse stationResponse = response.as(StationResponse.class);
        assertThat(stationResponse.getId()).isEqualTo(stationId);
    }

    @DisplayName("지하철역을 수정한다.")
    @Test
    void updateStation() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        final ExtractableResponse<Response> createResponse = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then().log().all()
                .extract();

        // when
        final Map<String, String> otherParams = new HashMap<>();
        otherParams.put("name", "삼성역");
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(otherParams)
                .when()
                .put(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        final ExtractableResponse<Response> createResponse = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines/stations")
                .then().log().all()
                .extract();

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에 역을 최초 등록한다.")
    @Test
    void addInitialStationInLine() throws JsonProcessingException {
        final LineNodeRequests request = new LineNodeRequests(LINE_NAME_2, List.of(A_NODE, B_NODE));

        final String json = jsonSerialize(request);

        given().body(json)
                .when().post("/lines/stations")
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("빈 노선에 하나의 역만 추가하는 경우 BadRequest를 반환한다.")
    @Test
    void addOneStationInEmptyLineThrowException() throws JsonProcessingException {
        final LineNodeRequests request = new LineNodeRequests(LINE_NAME_2, List.of(A_NODE));

        final String json = jsonSerialize(request);

        given().body(json)
                .when().post("/lines/stations")
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에서 역을 삭제한다.")
    @Test
    void deleteStationInLine() throws JsonProcessingException {
        final LineNodeRequests createRequest = new LineNodeRequests(LINE_NAME_2, List.of(A_NODE, B_NODE, C_NODE));
        final long deleteId = 2;

        final String json = jsonSerialize(createRequest);

        given().body(json)
                .when().post("/lines/stations")
                .then().statusCode(HttpStatus.CREATED.value());

        given()
                .when().delete("/lines/stations/" + deleteId)
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("남은 역이 하나인 경우 역도 함께 삭제된다.")
    @Test
    void deleteLine() throws JsonProcessingException {
        final LineNodeRequests createRequest = new LineNodeRequests(LINE_NAME_2, List.of(A_NODE, B_NODE));
        final long lineId = 1;
        final long deleteStationId = 2;

        final String json = jsonSerialize(createRequest);

        given().body(json)
                .when().post("/lines/stations")
                .then().statusCode(HttpStatus.CREATED.value());

        given()
                .when().delete("/lines/stations/" + deleteStationId)
                .then().statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .when().get("/lines/" + lineId)
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("노선의 역들을 조회한다.")
    @Test
    void findStationsByLine() throws JsonProcessingException {
        final long lineId = 1;
        final LineNodeRequests createRequest = new LineNodeRequests(LINE_NAME_2, List.of(A_NODE, B_NODE, C_NODE));

        final String json = jsonSerialize(createRequest);

        given().body(json)
                .when().post("/lines/stations")
                .then().statusCode(HttpStatus.CREATED.value());

        final String response = given()
                .when().get("/lines/" + lineId)
                .then().statusCode(HttpStatus.OK.value())
                .extract().asString();

        final StationResponse[] stations = OBJECT_MAPPER.readValue(response, StationResponse[].class);
        assertThat(stations)
                .extracting(StationResponse::getName)
                .containsExactly(A_NODE.getStationName(), B_NODE.getStationName(), C_NODE.getStationName());
    }

    @DisplayName("모든 노선과 노선에 포함된 역들을 조회한다.")
    @Test
    void findAllLinesAndStations() throws JsonProcessingException {
        final LineNodeRequests line2NodeRequests = new LineNodeRequests(LINE_NAME_2, List.of(A_NODE, B_NODE));
        final LineNodeRequests line3NodeRequests = new LineNodeRequests(LINE_NAME_3, List.of(B_NODE, C_NODE));

        given().body(jsonSerialize(line2NodeRequests))
                .when().post("/lines/stations")
                .then().statusCode(HttpStatus.CREATED.value());

        given().body(jsonSerialize(line3NodeRequests))
                .when().post("/lines/stations")
                .then().statusCode(HttpStatus.CREATED.value());

        final String response = given()
                .when().get("/lines")
                .then().extract().asString();

        final FinalLineResponse[] responses = OBJECT_MAPPER.readValue(response, FinalLineResponse[].class);

        assertThat(responses)
                .extracting(FinalLineResponse::getName)
                .containsExactlyInAnyOrder(line2NodeRequests.getName(), line3NodeRequests.getName());
        //TODO : 역들이 들어갔는 지까지 검증
    }
}
