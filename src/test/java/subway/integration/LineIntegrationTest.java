package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Station;
import subway.dto.LineCreateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {
    private LineCreateRequest lineCreateRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        lineCreateRequest = new LineCreateRequest(
                "잠실역", "잠실새내역", "2호선", 5);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLineTest() {
        // when
        ExtractableResponse<Response> response = createLine();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> createLine() {
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

        ExtractableResponse<Response> createResponse = createLine();
        Long id = createResponse.response().jsonPath().getLong("id");
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineCreateRequest)
                .when().get("/lines/" + id)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
