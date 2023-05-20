package subway.integration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;
import subway.dto.LineStationsResponse;
import subway.dto.StationResponse;

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

    @Sql("/InitializeTable.sql")
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = List.of(1L, 2L);

        List<LineStationsResponse> result = response.jsonPath().getList(".", LineStationsResponse.class);
        assertAll(
            () -> assertThat(result.stream()
                .map(lineStationsResponse -> lineStationsResponse.getLine().getId())
                .collect(Collectors.toUnmodifiableList()))
                .containsAll(expectedLineIds),

            () -> assertThat(result.stream()
                .filter(lineStationsResponse -> lineStationsResponse.getLine().getId() == 1L)
                .findFirst()
                .get()
                .getLineStations()).hasSize(0),

            () -> {
                List<StationResponse> lineStations = result.stream()
                    .filter(lineStationsResponse -> lineStationsResponse.getLine().getId() == 2L)
                    .findFirst()
                    .get()
                    .getLineStations();
                assertAll(
                    () -> assertThat(lineStations.get(0).getId()).isEqualTo(1L),
                    () -> assertThat(lineStations.get(0).getName()).isEqualTo("신도림"),
                    () -> assertThat(lineStations.get(1).getId()).isEqualTo(2L),
                    () -> assertThat(lineStations.get(1).getName()).isEqualTo("영등포구청"),
                    () -> assertThat(lineStations.get(2).getId()).isEqualTo(3L),
                    () -> assertThat(lineStations.get(2).getName()).isEqualTo("신대방")
                );
            });
    }

    @Sql("/InitializeTable.sql")
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}", 2L)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineStationsResponse resultResponse = response.as(LineStationsResponse.class);

        assertAll(
            () -> assertThat(resultResponse.getLine().getId()).isEqualTo(2L),
            () -> {
                List<StationResponse> lineStations = resultResponse.getLineStations();
                assertAll(
                    () -> assertThat(lineStations.get(0).getId()).isEqualTo(1L),
                    () -> assertThat(lineStations.get(0).getName()).isEqualTo("신도림"),
                    () -> assertThat(lineStations.get(1).getId()).isEqualTo(2L),
                    () -> assertThat(lineStations.get(1).getName()).isEqualTo("영등포구청"),
                    () -> assertThat(lineStations.get(2).getId()).isEqualTo(3L),
                    () -> assertThat(lineStations.get(2).getName()).isEqualTo("신대방")
                );
            });
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
            .when().patch("/lines/{lineId}", lineId)
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
