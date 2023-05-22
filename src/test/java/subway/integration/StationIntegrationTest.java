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

@DisplayName("지하철역 관련 기능")
public class StationIntegrationTest extends IntegrationTest {

    private LineRequest lineRequest1;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineRequest1 = new LineRequest("2호선", "#123456");
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM SECTION");
        jdbcTemplate.execute("DELETE FROM STATION");
        jdbcTemplate.execute("DELETE FROM line");
    }

    @DisplayName("지하철역 2개를 생성 및 초기 등록한다.")
    @Test
    void create_two_stations() {
        // given
        LineResponse lineResult = createLine();

        Map<String, Object> params = new HashMap<>();
        params.put("lineName", lineResult.getName());
        params.put("upBoundStationName", "잠실역");
        params.put("downBoundStationName", "선릉역");
        params.put("distance", 10);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations/init")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철 역 하나를 등록한다.")
    @Test
    void create_station() {
        // given
        LineResponse line = createLine();
        createStations(line);

        Map<String, Object> params = new HashMap<>();
        params.put("registerStationName", "강남역");
        params.put("lineName", line.getName());
        params.put("baseStation", "선릉역");
        params.put("direction", "right");
        params.put("distance", 10);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then()
                .log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void delete_Station() {
        // given
        LineResponse line = createLine();
        StationInitResponse stations = createStations(line);


        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete("/stations/" + stations.getUpboundStationId())
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private LineResponse createLine() {
        ExtractableResponse<Response> lineResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
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
