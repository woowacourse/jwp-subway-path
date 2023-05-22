package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.SectionRequest;
import subway.dto.StationResponse;
import subway.dto.StationsByLineResponse;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
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

    @Test
    void 지하철_노선을_생성한다() {
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

    @Test
    void 기존에_존재하는_노선_이름으로_노선을_생성한다() {
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


    @Test
    void 특정_노선의_중간_오른쪽에_역_추가() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(2L, 5L, 3);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + line.getId())
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 특정_노선의_중간_왼쪽에_역_추가() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(99L, 2L, 1);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + line.getId())
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 특정_노선의_오른쪽_끝에_역_추가() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(4L, 100L, 9);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + line.getId())
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 특정_노선의_왼쪽_끝에_역_추가() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(101L, 1L, 8);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + line.getId())
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void 이미_노선에_존재하는_두_역으로_구간_추가_시도할때_예외처리한다() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 8);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + line.getId())
                .then().log().all().extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.asString()).isEqualTo("해당 조건으로 역을 설치할 수 없습니다.");
        });
    }

    @Test
    void 중간에_추가하는_구간의_거리가_기존_구간_사이에_들어갈_수_없을_때() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        SectionRequest sectionRequest = new SectionRequest(1L, 10L, 99);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/" + line.getId())
                .then().log().all().
                extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.asString()).isEqualTo("삽입할 수 없는 거리입니다.");
        });
    }

    @Test
    void 특정_노선에서_역을_삭제한다() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        Station dinoStation = new Station(2L, "디노");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + line.getId() + "?stationId=" + dinoStation.getId())
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 특정_노선에_존재하지_않는_역을_삭제시_예외처라() {
        // given
        Line line = new Line(1L, "2호선", "Green");
        Station newStation = new Station(1000L, "굿다이노");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + line.getId() + "?stationId=" + newStation.getId())
                .then().log().all().
                extract();

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.asString()).isEqualTo("해당 역은 노선에 존재하지 않습니다.");
        });
    }

    @Test
    void 지하철_노선과_해당하는_역_조회() {
        Line line = new Line(1L, "2호선", "Green");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", line.getId())
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        StationsByLineResponse stationsByLineResponse = response.as(StationsByLineResponse.class);
        List<StationResponse> stationResponses = stationsByLineResponse.getStations();

        assertSoftly(softly -> {
            softly.assertThat(stationsByLineResponse.getLineId()).isEqualTo(1L);
            softly.assertThat(stationsByLineResponse.getName()).isEqualTo("2호선");
            softly.assertThat(stationsByLineResponse.getColor()).isEqualTo("Green");

            softly.assertThat(stationResponses.size()).isEqualTo(4);
            softly.assertThat(stationResponses.get(0).getId()).isEqualTo(1L);
            softly.assertThat(stationResponses.get(0).getName()).isEqualTo("후추");
            softly.assertThat(stationResponses.get(1).getId()).isEqualTo(2L);
            softly.assertThat(stationResponses.get(1).getName()).isEqualTo("디노");
            softly.assertThat(stationResponses.get(2).getId()).isEqualTo(3L);
            softly.assertThat(stationResponses.get(2).getName()).isEqualTo("조앤");
            softly.assertThat(stationResponses.get(3).getId()).isEqualTo(4L);
            softly.assertThat(stationResponses.get(3).getName()).isEqualTo("로운");
        });
    }

    @Test
    void 모든_지하철_노선과_해당하는_역을_차례로_조회() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract();

        List<StationsByLineResponse> stationsByLineResponses = response.jsonPath()
                .getList(".", StationsByLineResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(stationsByLineResponses.size()).isEqualTo(2);

            StationsByLineResponse stationsByLineResponse1 = stationsByLineResponses.get(0);
            List<StationResponse> stations1 = stationsByLineResponse1.getStations();

            softly.assertThat(stationsByLineResponse1.getLineId()).isEqualTo(1L);
            softly.assertThat(stationsByLineResponse1.getName()).isEqualTo("2호선");
            softly.assertThat(stationsByLineResponse1.getColor()).isEqualTo("Green");
            softly.assertThat(stations1.size()).isEqualTo(4);
            softly.assertThat(stations1.get(0).getId()).isEqualTo(1L);
            softly.assertThat(stations1.get(0).getName()).isEqualTo("후추");
            softly.assertThat(stations1.get(1).getId()).isEqualTo(2L);
            softly.assertThat(stations1.get(1).getName()).isEqualTo("디노");
            softly.assertThat(stations1.get(2).getId()).isEqualTo(3L);
            softly.assertThat(stations1.get(2).getName()).isEqualTo("조앤");
            softly.assertThat(stations1.get(3).getId()).isEqualTo(4L);
            softly.assertThat(stations1.get(3).getName()).isEqualTo("로운");

            StationsByLineResponse stationsByLineResponse2 = stationsByLineResponses.get(1);
            List<StationResponse> stations2 = stationsByLineResponse2.getStations();

            softly.assertThat(stationsByLineResponse2.getLineId()).isEqualTo(2L);
            softly.assertThat(stationsByLineResponse2.getName()).isEqualTo("8호선");
            softly.assertThat(stationsByLineResponse2.getColor()).isEqualTo("pink");
            softly.assertThat(stations2.size()).isEqualTo(3);
            softly.assertThat(stations2.get(0).getId()).isEqualTo(3L);
            softly.assertThat(stations2.get(0).getName()).isEqualTo("조앤");
            softly.assertThat(stations2.get(1).getId()).isEqualTo(5L);
            softly.assertThat(stations2.get(1).getName()).isEqualTo("포비");
            softly.assertThat(stations2.get(2).getId()).isEqualTo(4L);
            softly.assertThat(stations2.get(2).getName()).isEqualTo("로운");
        });
    }

    @Test
    void 지하철_노선_정보를_수정한다() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
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

}
