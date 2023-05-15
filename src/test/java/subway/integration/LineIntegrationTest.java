package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Station;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineCreateRequest lineCreateRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        lineCreateRequest = new LineCreateRequest(
                "2호선",
                "잠실역",
                "잠실새내역",
                5);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLineTest() {
        // when
        ExtractableResponse<Response> response = createLine(lineCreateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> createLine(LineCreateRequest lineCreateRequest) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().post("/lines")
                .then().log().all().
                extract();
        return response;
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLine() {

        ExtractableResponse<Response> createResponse = createLine(lineCreateRequest);
        Long id = createResponse.response().jsonPath().getLong("id");
        LineResponse lineResponse = createResponse.as(LineResponse.class);
        Long id1 = lineResponse.getId();
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().get("/lines/" + id1)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("모든 지하철 노선을 조회한다.")
    @Test
    void findALlLines() {
        createLine(lineCreateRequest);
        LineCreateRequest lineCreateRequest1 = new LineCreateRequest(
                "3호선",
                "1번",
                "2번",
                5);
        createLine(lineCreateRequest1);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/")
                .then().log().all().
                extract();

        List<LineResponse> lineResponses = response.as(
                new ParameterizedTypeReference<List<LineResponse>>() {
                }.getType());
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponses.get(0).getName()).isEqualTo("2호선");
        assertThat(lineResponses.get(0).getStations().get(0).getName()).isEqualTo("잠실역");
        assertThat(lineResponses.get(0).getStations().get(1).getName()).isEqualTo("잠실새내역");
        assertThat(lineResponses.get(1).getName()).isEqualTo("3호선");
        assertThat(lineResponses.get(1).getStations().get(0).getName()).isEqualTo("1번");
        assertThat(lineResponses.get(1).getStations().get(1).getName()).isEqualTo("2번");
    }
}