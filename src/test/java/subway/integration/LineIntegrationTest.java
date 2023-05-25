package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.domain.section.domain.Distance;
import subway.line.presentation.dto.SurchargeRequest;
import subway.line.presentation.dto.LineRequest;
import subway.line.presentation.dto.LineResponse;
import subway.line.presentation.dto.SectionSavingRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

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
        saveLine(lineRequest1);

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
        final var lineId1 = saveLine(lineRequest1);
        final var lineId2 = saveLine(lineRequest2);

        final var 개룡역 = saveStation("개룡역");
        final var 거여역 = saveStation("거여역");
        saveSection(lineId1, 개룡역, 거여역);

        final var 용인역 = saveStation("용인역");
        final var 강릉역 = saveStation("강릉역");
        saveSection(lineId2, 용인역, 강릉역);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> responses = response.jsonPath().getList(".", LineResponse.class);
        for (LineResponse lineResponse : responses) {
            assertThat(lineResponse.getStations()).hasSize(2);
        }
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final var lineId = saveLine(lineRequest1);
        final var requestUri = "lines/" + lineId;

        final var stationIdGR = saveStation("개룡역");
        final var stationIdGY = saveStation("거여역");
        saveSection(lineId, stationIdGR, stationIdGY);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(requestUri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse resultResponse = response.as(LineResponse.class);
        assertThat(resultResponse.getId()).isEqualTo(lineId);
        assertThat(resultResponse.getStations()).hasSize(2);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final var lineId = saveLine(lineRequest1);

        // when
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
        final var lineId = saveLine(lineRequest1);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("특정 노선에 대한 추가요금을 등록한다.")
    void saveFare() {
        final var 일호선 = saveLine(new LineRequest("1호선", "yellow"));

        final var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SurchargeRequest("900"))
                .when().post("/lines/" + 일호선 + "/surcharge")
                .then().log().all()
                .extract();

        assertThat(response.statusCode())
                .isEqualTo(201);
    }

    private static void saveSection(long lineId, long previousStationId, long nextStationId) {
        SectionSavingRequest sectionSavingRequest
                = new SectionSavingRequest(previousStationId, nextStationId, Distance.of(5), true);
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionSavingRequest)
                .when().post("/lines/" + lineId + "/section")
                .then().log().all();
    }

    private static long saveStation(String name) {
        final var location = RestAssured.given().log().all()
                .body(Map.of("name", name))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract()
                .header("location");

        return Long.parseLong(location.replace("/stations/", ""));
    }

    private long saveLine(LineRequest lineRequest) {
        final var location = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .header("location");

        return Long.parseLong(location.replace("/lines/", ""));
    }
}
