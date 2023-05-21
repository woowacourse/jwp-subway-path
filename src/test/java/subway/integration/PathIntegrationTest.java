package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.PathResponse;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;

@DisplayName("경로 관련 기능")
public class PathIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("두 역 사이의 최단 경로를 구한다.")
    void getPath() {
        // given
        final ExtractableResponse<Response> lineResponse1 = postLine(new LineRequest("1호선", "bg-red-600"));
        final ExtractableResponse<Response> lineResponse2 = postLine(new LineRequest("2호선", "bg-green-600"));

        final ExtractableResponse<Response> stationResponse1 = postStation(new StationRequest("반월당역"));
        final ExtractableResponse<Response> stationResponse2 = postStation(new StationRequest("경대병원역"));
        final ExtractableResponse<Response> stationResponse3 = postStation(new StationRequest("청라언덕역"));

        postSection(new SectionRequest(extractId(lineResponse1), extractId(stationResponse1), extractId(stationResponse2), 10));
        postSection(new SectionRequest(extractId(lineResponse2), extractId(stationResponse3), extractId(stationResponse1), 6));

        // when
        final ExtractableResponse<Response> result = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/paths?upStationId=" + extractId(stationResponse3)
                        + "&downStationId=" + extractId(stationResponse2))
                .then().log().all()
                .extract();

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        final PathResponse pathResponse = result.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(16);
        assertThat(pathResponse.getFare()).isEqualTo(1450);
        assertThat(pathResponse.getStations())
                .usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse3, stationResponse1, stationResponse2));
    }

    private long extractId(final ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    private ExtractableResponse<Response> postLine(final LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> postStation(final StationRequest stationRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRequest)
                .when().post("/stations")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> postSection(final SectionRequest sectionRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/sections")
                .then().extract();
    }
}
