package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationEnrollRequest;
import subway.dto.StationRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600");
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
    void getLines() {
        // given
        final ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        final ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        final List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
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
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineId);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
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
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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

    /////////////////////////
    @Test
    @DisplayName("지하철 노선에 역을 추가할 수 있다")
    void test_addStationToLine() {
        //given
        final LineRequest lineRequest = new LineRequest("2호선", "초록");

        final long lineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final StationRequest jamsilRequest = new StationRequest("잠실역");
        final StationRequest bangbaeRequest = new StationRequest("방배역");

        final long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        final StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        //when, then
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/lines/{lineId}/stations", lineId)

                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("지하철 노선에서 역을 삭제할 수 있다")
    void test_deleteStationFromLine() {
        //given
        final LineRequest lineRequest = new LineRequest("2호선", "초록");

        final long lineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final StationRequest jamsilRequest = new StationRequest("잠실역");
        final StationRequest bangbaeRequest = new StationRequest("방배역");

        final long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        final StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/lines/{lineId}/stations", lineId);

        // when, then
        RestAssured.given()
                .when().delete("/lines/{lineId}/stations/{stationId}", lineId, jamsilStationId)

                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("지하철 노선의 역들을 조회할 수 있다.")
    void test_findStationFromLine() {
        //given
        final LineRequest lineRequest = new LineRequest("2호선", "초록");

        final long lineId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final StationRequest jamsilRequest = new StationRequest("잠실역");
        final StationRequest bangbaeRequest = new StationRequest("방배역");

        final long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        final StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/lines/{lineId}/stations", lineId);

        // when, then
        final LineResponse response = RestAssured.given()
                .queryParam("withStation", "true")
                .when().get("/lines/{lineId}", lineId)

                .then()
                .extract()
                .body()
                .as(LineResponse.class);

        assertAll(
                () -> assertThat(response.getId()).isEqualTo(lineId),
                () -> assertThat(response.getName()).isEqualTo("2호선"),
                () -> assertThat(response.getStationResponses()).hasSize(2)
        );
    }

    @Test
    @DisplayName("모든 지하철 노선의 역들을 조회할 수 있다.")
    void test_findAllStationFromLine() {
        //given
        final LineRequest lineOneRequest = new LineRequest("1호선", "파랑");
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineOneRequest)

                .when().post("/lines");

        final LineRequest lineTwoRequest = new LineRequest("2호선", "초록");

        final long lineTwoId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineTwoRequest)

                .when().post("/lines")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final StationRequest jamsilRequest = new StationRequest("잠실역");
        final StationRequest bangbaeRequest = new StationRequest("방배역");

        final long jamsilStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(jamsilRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath()
                .getLong("id");

        final long bangbaeStationId = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(bangbaeRequest)

                .when().post("/stations")

                .then()
                .extract()
                .jsonPath().getLong("id");

        final StationEnrollRequest stationEnrollRequest = new StationEnrollRequest(
                jamsilStationId, bangbaeStationId, 3);

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEnrollRequest)

                .when().post("/lines/{lineId}/stations", lineTwoId);

        // when, then
        RestAssured.given()
                .queryParam("withStation", "true")
                .when().get("/lines")

                .then()
                .body("size()", is(2))
                .body("[0].stationResponses.size()", is(0))
                .body("[1].stationResponses.size()", is(2));
    }
}
