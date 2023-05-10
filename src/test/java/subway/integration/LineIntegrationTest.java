package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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

    // TODO
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        ExtractableResponse<Response> createResponse2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().post("/lines")
                .then().log().all().
                extract();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        LineResponse second = LineResponse.of(new LineEntity("2호선", "green"));
        StationResponse 잠실역 = StationResponse.of(new StationEntity("잠실역"));
        StationResponse 강남역 = StationResponse.of(new StationEntity("강남역"));
        Map<LineResponse, List<StationResponse>> expectedResponse = new HashMap<>();
        expectedResponse.put(second, List.of(잠실역, 강남역));

        Map<LineResponse, List<StationResponse>> responses = response.jsonPath().getMap(".");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(responses.keySet().size()).isEqualTo(1);
        assertThat(responses.values().size()).isEqualTo(1);
    }

    // TODO
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
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
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        List<StationResponse> stationResponses = response.jsonPath().getList(".", StationResponse.class);
        StationResponse 잠실역 = StationResponse.of(new StationEntity("잠실역"));
        StationResponse 선릉역 = StationResponse.of(new StationEntity("선릉역"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationResponses).usingRecursiveComparison().isEqualTo(List.of(잠실역, 선릉역));
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


    @DisplayName("특정 노선의 구간 목록을 조회한다.")
    @Test
    void getSections() {
        /// given
//        Map<String, String> params1 = new HashMap<>();
//        params1.put("name", "강남역");
//        params1.put("lineId", "2");
//        params1.put("upStationId", "4");
//
//        ExtractableResponse<Response> createdResponse1 = RestAssured.given().log().all()
//                .body(params1)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when()
//                .post("/sections")
//                .then().log().all()
//                .extract();
//
//
//        Map<String, String> params2 = new HashMap<>();
//        params2.put("name", "선릉역");
//        params2.put("lineId", "2");
//        params2.put("upStationId", "5");
//
//        ExtractableResponse<Response> createdResponse2 = RestAssured.given().log().all()
//                .body(params1)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .when()
//                .post("/sections")
//                .then().log().all()
//                .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/{id}", 2)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        List<Long> expectedStationIds = Stream.of(createResponse1, createResponse2)
//                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
//                .collect(Collectors.toList());
        List<StationResponse> resultStationIds = new ArrayList<>(response.jsonPath().getList(".", StationResponse.class));
        StationResponse 선릉역 = StationResponse.of(new StationEntity("선릉역"));
        StationResponse 잠실역 = StationResponse.of(new StationEntity("잠실역"));
        assertThat(resultStationIds).usingRecursiveComparison().isEqualTo(List.of(잠실역, 선릉역));
    }
}
