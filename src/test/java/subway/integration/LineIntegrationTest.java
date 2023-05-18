package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        lineRequest1 = new LineRequest("1호선", "bg-red-600");
        lineRequest2 = new LineRequest("2호선", "bg-green-600");
    }

    private ExtractableResponse<Response> postLine(final LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
    }

    private ExtractableResponse<Response> postStation(final StationRequest stationRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then().extract();
    }

    private ExtractableResponse<Response> postSection(final SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = postLine(lineRequest1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성하면 500 상태 코드를 반환한다.")
    @Test
    void createLineWithDuplicateName() {
        // given
        postLine(lineRequest1);

        // when
        ExtractableResponse<Response> response = postLine(lineRequest1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 모든 노선을 조회한다.")
    @Test
    void getLines() {
        // given
        final ExtractableResponse<Response> lineResponse1 = postLine(lineRequest1);
        final ExtractableResponse<Response> lineResponse2 = postLine(lineRequest2);

        final StationRequest stationRequest1 = new StationRequest("동대구역");
        final ExtractableResponse<Response> stationResponse1 = postStation(stationRequest1);
        final StationRequest stationRequest2 = new StationRequest("신천역");
        final ExtractableResponse<Response> stationResponse2 = postStation(stationRequest2);

        final Long lineId = Long.parseLong(lineResponse1.header("Location").split("/")[2]);
        final Long upStationId = Long.parseLong(stationResponse1.header("Location").split("/")[2]);
        final Long downStationId = Long.parseLong(stationResponse2.header("Location").split("/")[2]);
        final SectionRequest sectionRequest = new SectionRequest(lineId, upStationId, downStationId, 10);
        postSection(sectionRequest);

        // when
        ExtractableResponse<Response> sectionResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().
                extract();

        // then
        List<String> stationResponses = sectionResponse.jsonPath().getList("stationResponses.name", String.class);
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationResponses.get(0)).contains("동대구역", "신천역");
    }

    @DisplayName("지하철 특정 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        final ExtractableResponse<Response> lineResponse = postLine(lineRequest2);

        final StationRequest stationRequest1 = new StationRequest("경대병원역");
        final ExtractableResponse<Response> stationResponse1 = postStation(stationRequest1);
        final StationRequest stationRequest2 = new StationRequest("대구은행역");
        final ExtractableResponse<Response> stationResponse2 = postStation(stationRequest2);

        final Long lineId = Long.parseLong(lineResponse.header("Location").split("/")[2]);
        final Long upStationId = Long.parseLong(stationResponse1.header("Location").split("/")[2]);
        final Long downStationId = Long.parseLong(stationResponse2.header("Location").split("/")[2]);
        final SectionRequest sectionRequest = new SectionRequest(lineId, upStationId, downStationId, 10);
        postSection(sectionRequest);

        // when
        ExtractableResponse<Response> sectionResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all().
                extract();

        // then
        List<String> stationResponses = sectionResponse.jsonPath().getList("stationResponses.name", String.class);
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationResponses).containsAnyOf("경대병원역", "대구은행역");
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final ExtractableResponse<Response> createResponse = postLine(lineRequest1);

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
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
        final ExtractableResponse<Response> createResponse = postLine(lineRequest1);

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
