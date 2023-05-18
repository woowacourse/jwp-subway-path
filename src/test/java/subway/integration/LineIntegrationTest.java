package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.request.LineRequest;
import subway.controller.dto.response.SingleLineResponse;
import subway.controller.dto.response.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
class LineIntegrationTest extends IntegrationTest {

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        lineRequest1 = new LineRequest("5호선", "bg-red-600", 10, "잠실", "잠실새내");
        lineRequest2 = new LineRequest("6호선", "bg-olive-600", 4, "잠실", "종합운동장");
    }

    /**
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    @Sql("/station_test_data.sql")
    void createLine() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    @Sql("/station_test_data.sql")
    void createLineWithDuplicateName() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    @Sql("/station_test_data.sql")
    void getLine() {
        ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/lines")
                .then().log().all().
                extract();

        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{lineId}", lineId)
                .then().log().all()
                .extract();

        StationResponse 잠실 = new StationResponse(1L, "잠실");
        StationResponse 잠실새내 = new StationResponse(2L, "잠실새내");

        SingleLineResponse lineResponse = response.as(SingleLineResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineResponse.getLinePropertyResponse().getId()).isEqualTo(1L);
        assertThat(lineResponse.getLinePropertyResponse().getName()).isEqualTo("5호선");
        assertThat(lineResponse.getLinePropertyResponse().getColor()).isEqualTo("bg-red-600");
        assertThat(lineResponse.getStationResponses())
                .containsAll(List.of(잠실, 잠실새내));
    }

    /**
     * INSERT INTO station(name) VALUES('잠실'), ('잠실새내'), ('종합운동장'), ('석촌'), ('송파');
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    @Sql("/station_test_data.sql")
    void getLines() {
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

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        // then
        StationResponse 잠실 = new StationResponse(1L, "잠실");
        StationResponse 잠실새내 = new StationResponse(2L, "잠실새내");
        StationResponse 종합운동장 = new StationResponse(3L, "종합운동장");
        List<SingleLineResponse> result = new ArrayList<>(response.jsonPath().getList(".", SingleLineResponse.class));
        List<Long> expectedLineIds = Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        assertThat(expectedLineIds).containsAll(List.of(1L, 2L));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).hasSize(2);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.get(0).getLinePropertyResponse().getId()).isEqualTo(1L);
        assertThat(result.get(0).getLinePropertyResponse().getName()).isEqualTo("5호선");
        assertThat(result.get(0).getLinePropertyResponse().getColor()).isEqualTo("bg-red-600");
        assertThat(result.get(0).getStationResponses()).containsAll(List.of(잠실, 잠실새내));
        assertThat(result.get(1).getLinePropertyResponse().getId()).isEqualTo(2L);
        assertThat(result.get(1).getLinePropertyResponse().getName()).isEqualTo("6호선");
        assertThat(result.get(1).getLinePropertyResponse().getColor()).isEqualTo("bg-olive-600");
        assertThat(result.get(1).getStationResponses()).containsAll(List.of(잠실새내, 종합운동장));
    }

//    @DisplayName("지하철 노선을 수정한다.")
//    @Test
//    @Sql("/station_test_data.sql")
//    void updateLine() {
//        // given
//        ExtractableResponse<Response> createResponse = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest1)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//
//        // when
//        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest2)
//                .when().put("/lines/{lineId}", lineId)
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//    }
//
//    @DisplayName("지하철 노선을 제거한다.")
//    @Test
//    @Sql("/station_test_data.sql")
//    void deleteLine() {
//        // given
//        ExtractableResponse<Response> createResponse = RestAssured
//                .given().log().all()
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .body(lineRequest1)
//                .when().post("/lines")
//                .then().log().all().
//                extract();
//
//        // when
//        Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
//        ExtractableResponse<Response> response = RestAssured
//                .given().log().all()
//                .when().delete("/lines/{lineId}", lineId)
//                .then().log().all()
//                .extract();
//
//        // then
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
//    }

}
