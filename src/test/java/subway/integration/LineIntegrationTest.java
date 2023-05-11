package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.LineRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql({"/test-data.sql"})
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

    @DisplayName("지하철 모든 노선을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> lineResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().extract();

        ExtractableResponse<Response> lineResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().extract();

        Map<String, String> stationParams1 = new HashMap<>();
        stationParams1.put("name", "해운대역");
        ExtractableResponse<Response> stationResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationParams1)
                .when().post("/stations")
                .then().extract();

        Map<String, String> stationParams2 = new HashMap<>();
        stationParams2.put("name", "동대구역");
        ExtractableResponse<Response> stationResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationParams2)
                .when().post("/stations")
                .then().extract();

        Map<String, String> sectionParams = new HashMap<>();
        sectionParams.put("lineId", "1");
        sectionParams.put("stationId", "1");
        sectionParams.put("upStationId", "2");
        sectionParams.put("distance", "3");

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionParams)
                .when().post("/sections")
                .then().log().all().extract();

        Map<String, String> stationParams3 = new HashMap<>();
        stationParams2.put("name", "신천역");
        ExtractableResponse<Response> stationResponse3 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationParams2)
                .when().post("/stations")
                .then().extract();

        Map<String, String> sectionParams2 = new HashMap<>();
        sectionParams.put("lineId", "2");
        sectionParams.put("stationId", "2");
        sectionParams.put("upStationId", "3");
        sectionParams.put("distance", "3");

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionParams)
                .when().post("/sections")
                .then().log().all().extract();

        ExtractableResponse<Response> sectionResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().
                extract();

        // then

        List<String> stationResponses = sectionResponse.jsonPath().getList("stationResponses.name", String.class);
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationResponses.get(0)).contains("해운대역", "동대구역");
    }

    @DisplayName("지하철 특정 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().extract();

        Map<String, String> stationParams1 = new HashMap<>();
        stationParams1.put("name", "해운대역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationParams1)
                .when().post("/stations")
                .then().extract();

        Map<String, String> stationParams2 = new HashMap<>();
        stationParams2.put("name", "동대구역");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationParams2)
                .when().post("/stations")
                .then().extract();

        Map<String, String> sectionParams = new HashMap<>();
        sectionParams.put("lineId", "1");
        sectionParams.put("stationId", "1");
        sectionParams.put("upStationId", "2");
        sectionParams.put("distance", "3");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionParams)
                .when().post("/sections")
                .then().extract();

        ExtractableResponse<Response> sectionResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", 1)
                .then().log().all().
                extract();

        // then

        List<String> stationResponses = sectionResponse.jsonPath().getList("stationResponses.name", String.class);
        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationResponses).containsAnyOf("해운대역", "동대구역");
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
