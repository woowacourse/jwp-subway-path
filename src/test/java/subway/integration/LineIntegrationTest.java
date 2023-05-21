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
import subway.dto.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.integration.Utils.*;

public class LineIntegrationTest extends IntegrationTest {
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLineTest() {
        ExtractableResponse<Response> response = createLine("2호선");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @Test
    @DisplayName("중복된 노선을 생성하는 경우 CONFLICT 에러가 발생한다.")
    void createDuplicateLine() {
        initLine("3호선",
                "1번",
                "2번",
                5);

        ExtractableResponse<Response> response = createLine("3호선");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLine() {
        ExtractableResponse<Response> createResponse = createLine("2호선");
        addStation("2호선",
                "잠실역",
                "잠실새내역",
                5);
        LineResponse lineResponse = createResponse.as(LineResponse.class);
        Long id = lineResponse.getId();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all().
                extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 조회한다. - 노선에 역이 존재하지 않는 경우")
    @Test
    void findLine1() {
        ExtractableResponse<Response> createResponse = createLine("2호선");
        LineResponse lineResponse = createResponse.as(LineResponse.class);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + lineResponse.getId())
                .then().log().all().
                extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("모든 지하철 노선을 조회한다.")
    @Test
    void findAllLines() {
        initLine("2호선",
                "잠실역",
                "잠실새내역",
                3);
        initLine("3호선",
                "1번",
                "2번",
                5);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/")
                .then().log().all().
                extract();

        List<LineResponse> lineResponses = response.as(
                new ParameterizedTypeReference<List<LineResponse>>() {
                }.getType());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponses.get(0).getName()).isEqualTo("2호선");
        assertThat(lineResponses.get(0).getStations().get(0).getName()).isEqualTo("잠실역");
        assertThat(lineResponses.get(0).getStations().get(1).getName()).isEqualTo("잠실새내역");
        assertThat(lineResponses.get(1).getName()).isEqualTo("3호선");
        assertThat(lineResponses.get(1).getStations().get(0).getName()).isEqualTo("1번");
        assertThat(lineResponses.get(1).getStations().get(1).getName()).isEqualTo("2번");
    }

}