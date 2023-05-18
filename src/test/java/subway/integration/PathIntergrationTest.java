package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationInitResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("경로 관련 기능")
public class PathIntergrationTest extends IntegrationTest {

    private LineRequest lineRequest;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest = new LineRequest("2호선", "#123456");
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM SECTION");
        jdbcTemplate.execute("DELETE FROM STATION");
        jdbcTemplate.execute("DELETE FROM line");
    }

    @DisplayName("최단 경로를 조회한다.")
    @Test
    void find_short_path() {
        // given
        LineResponse line = createLine();
        StationInitResponse stations = createStations(line);

        Map<String, Object> params = new HashMap<>();
        params.put("departureLine", line.getName());
        params.put("departureStation", "잠실역");
        params.put("arrivalLine", line.getName());
        params.put("arrivalStation", "선릉역");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().get("/paths")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private LineResponse createLine() {
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest)
                .when().post("/lines")
                .then().log().all().
                extract();

        LineResponse lineResult = lineResponse.body().as(LineResponse.class);
        return lineResult;
    }

    private StationInitResponse createStations(LineResponse lineResult) {
        Map<String, Object> params = new HashMap<>();
        params.put("lineName", lineResult.getName());
        params.put("upBoundStationName", "잠실역");
        params.put("downBoundStationName", "선릉역");
        params.put("distance", 10);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations/init")
                .then().log().all()
                .extract();

        return response.body().as(StationInitResponse.class);
    }
}
