package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationAddRequest;
import subway.dto.StationAddRequests;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    private static final LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
    private static final LineRequest lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
    private static final StationAddRequest stationAddRequest = new StationAddRequest(1L, 2L, 11);
    private static final StationAddRequests stationAddRequests = new StationAddRequests(List.of(
        new StationAddRequest(1L, 3L, 4),
        new StationAddRequest(3L, 2L, 7)
    ));


    @BeforeEach
    public void setUp() {
        super.setUp();
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

    @DisplayName("빈 노선에 2개의 역을 추가한다.")
    @Test
    void addStation_success_emptyLine() {
        //given
        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequest)
            .when().post("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + lineId);
    }

    @DisplayName("빈 노선에 역 1개를 추가하면 BadRequest를 응답한다.")
    @Test
    void addStation_fail_emptyLineWithOneStation() {
        //given
        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequests)
            .when().post("/lines/{lineId}/stations", lineId)
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("비어있지 않은 노선에 역을 1개 추가한다.")
    @Test
    void addStation_success() {
        //given
        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);

        StationAddRequest stationAddRequest = new StationAddRequest(1L, 2L, 11);
        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequest)
            .when().post("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequests)
            .when().post("/lines/{lineId}/stations", lineId)
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isEqualTo("/lines/" + lineId);
    }

    @DisplayName("비어있지 않은 노선에 역을 2개 추가하면 BadRequest를 응답한다.")
    @Test
    void addStation_fail_noEmptyLineWithTwoStation() {
        //given
        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequest)
            .when().post("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequest)
            .when().post("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("새로운 역을 추가할 때 맞지 않는 거리를 입력하면 BadRequest를 응답한다.")
    @CsvSource(value = {"1, 2", "10, 20", "11, 0"})
    @ParameterizedTest
    void addStation_fail_invalidDistance(int previousDistance, int nextDistance) {
        //given
        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequest)
            .when().post("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        StationAddRequests stationAddRequests = new StationAddRequests(List.of(
            new StationAddRequest(1L, 3L, previousDistance),
            new StationAddRequest(3L, 2L, nextDistance)
        ));

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequests)
            .when().post("/lines/{lineId}/stations", lineId)
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에서 역을 제거한다.")
    @Test
    void removeStation_success() {
        //given
        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);

        long stationId = 1L;

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(stationAddRequest)
            .when().post("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        //when
        ExtractableResponse<Response> response = RestAssured
            .when()
            .delete("/lines/{lineId}/stations/{stationId}", lineId, stationId)
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에서 역을 제거한다.")
    @Test
    void removeStation_success_noStation() {
        //given
        ExtractableResponse<Response> enrollResponse = enrollLine(lineRequest1);
        long lineId = Long.parseLong(enrollResponse.header("Location").split("/")[2]);

        //when
        ExtractableResponse<Response> response = RestAssured
            .when()
            .delete("/lines/{lineId}/stations/{stationId}", lineId, 1L)
            .then().log().all()
            .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> enrollLine(final LineRequest lineRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest)
            .when().post("/lines")
            .then().log().all().
            extract();
    }
}
