package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.dto.StationsByLineResponse;

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


    @DisplayName("지하철 노선과 해당하는 역을 조회한다.")
    @Test
    void findStationsByLineId() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        StationsByLineResponse stationsByLineResponse = response.as(StationsByLineResponse.class);
        LineResponse lineResponse = stationsByLineResponse.getLineResponse();
        List<StationResponse> stationResponses = stationsByLineResponse.getStationResponses();

        assertSoftly(softly -> {
            softly.assertThat(lineResponse.getId()).isEqualTo(1L);
            softly.assertThat(lineResponse.getName()).isEqualTo("2호선");
            softly.assertThat(lineResponse.getColor()).isEqualTo("Green");

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

    @DisplayName("모든 지하철 노선과 해당하는 역을 차례로 조회한다.")
    @Test
    void findLines() {
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
            LineResponse line1 = stationsByLineResponse1.getLineResponse();
            List<StationResponse> stations1 = stationsByLineResponse1.getStationResponses();

            softly.assertThat(line1.getId()).isEqualTo(1L);
            softly.assertThat(line1.getName()).isEqualTo("2호선");
            softly.assertThat(line1.getColor()).isEqualTo("Green");
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
            LineResponse line2 = stationsByLineResponse2.getLineResponse();
            List<StationResponse> stations2 = stationsByLineResponse2.getStationResponses();

            softly.assertThat(line2.getId()).isEqualTo(2L);
            softly.assertThat(line2.getName()).isEqualTo("8호선");
            softly.assertThat(line2.getColor()).isEqualTo("pink");
            softly.assertThat(stations2.size()).isEqualTo(3);
            softly.assertThat(stations2.get(0).getId()).isEqualTo(3L);
            softly.assertThat(stations2.get(0).getName()).isEqualTo("조앤");
            softly.assertThat(stations2.get(1).getId()).isEqualTo(5L);
            softly.assertThat(stations2.get(1).getName()).isEqualTo("포비");
            softly.assertThat(stations2.get(2).getId()).isEqualTo(4L);
            softly.assertThat(stations2.get(2).getName()).isEqualTo("로운");
        });
    }


    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
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
