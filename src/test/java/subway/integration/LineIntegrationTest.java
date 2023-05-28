package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionCreateRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.StationResponse;
import subway.exception.ExceptionResponse;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("신분당선", "bg-red-600", 100);
        lineRequest2 = new LineRequest("구신분당선", "bg-red-600", 0);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createTestLine() {
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

    @DisplayName("지하철 노선의 추가 요금이 0보다 작을 경우 생성할 수 없다.")
    @Test
    void createTestLine_fail_additionalFareMinus() {
        //given
        final LineRequest request = new LineRequest("신분당선", "빨강", -1);

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/lines")
            .then().log().all().
            extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.body().as(ExceptionResponse.class).getMessage()).isNotNull();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        addLineAndReturnLineId(lineRequest1);

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
        final Long lineId1 = addLineAndReturnLineId(lineRequest1);
        final Long lineId2 = addLineAndReturnLineId(lineRequest2);
        addTwoSection(lineId1);
        addTwoSection(lineId2);

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();

        // then
        final List<LineResponse> lineResponses = response.jsonPath().getList(".", LineResponse.class);
        assertAll(
            () -> assertThat(lineResponses).hasSize(2),
            () -> assertThat(lineResponses.get(0).getId()).isEqualTo(lineId1),
            () -> assertThat(lineResponses.get(0).getStations()).hasSize(3),
            () -> assertThat(lineResponses.get(0).getStations().get(0).getName()).isEqualTo("첫째역"),
            () -> assertThat(lineResponses.get(0).getStations().get(1).getName()).isEqualTo("둘째역"),
            () -> assertThat(lineResponses.get(0).getStations().get(2).getName()).isEqualTo("셋째역"),
            () -> assertThat(lineResponses.get(1).getId()).isEqualTo(lineId2),
            () -> assertThat(lineResponses.get(1).getStations()).hasSize(3),
            () -> assertThat(lineResponses.get(1).getStations().get(0).getName()).isEqualTo("첫째역"),
            () -> assertThat(lineResponses.get(1).getStations().get(1).getName()).isEqualTo("둘째역"),
            () -> assertThat(lineResponses.get(1).getStations().get(2).getName()).isEqualTo("셋째역")
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final Long lineId = addLineAndReturnLineId(lineRequest1);
        addTwoSection(lineId);

        // when
        final ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        // then
        final LineResponse resultResponse = response.as(LineResponse.class);
        final List<StationResponse> stationReponses = resultResponse.getStations();

        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(resultResponse.getId()).isEqualTo(lineId),
            () -> assertThat(stationReponses).hasSize(3),
            () -> assertThat(stationReponses.get(0).getName()).isEqualTo("첫째역"),
            () -> assertThat(stationReponses.get(1).getName()).isEqualTo("둘째역"),
            () -> assertThat(stationReponses.get(2).getName()).isEqualTo("셋째역")
        );
    }

    private Long addLineAndReturnLineId(final LineRequest request) {
        final ExtractableResponse<Response> createResponse = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/lines")
            .then().log().all().
            extract();

        return Long.parseLong(createResponse.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> addLineAndReturnResponse(final LineRequest request) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/lines")
            .then().log().all().
            extract();
    }

    private void addTwoSection(final Long lineId) {
        final SectionCreateRequest firstRequest = new SectionCreateRequest("첫째역", "둘째역", "하행", 10);

        RestAssured.given().log().all()
            .body(firstRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();

        final SectionCreateRequest secondRequest = new SectionCreateRequest("둘째역", "셋째역", "하행", 10);

        RestAssured.given().log().all()
            .body(secondRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/" + lineId + "/sections")
            .then().log().all()
            .extract();
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final Long lineId = addLineAndReturnLineId(lineRequest1);

        //when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(lineRequest2)
            .when().put("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        final Long lineId = addLineAndReturnLineId(lineRequest1);

        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().delete("/lines/{lineId}", lineId)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
