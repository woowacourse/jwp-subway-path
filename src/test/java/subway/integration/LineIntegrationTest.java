package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.line.domain.ShortestPath;
import subway.domain.line.dto.LineResponse;
import subway.domain.lineDetail.dto.LineDetailResponse;
import subway.domain.station.dto.StationResponse;
import subway.domain.station.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 지도 관련 기능")
class LineIntegrationTest extends IntegrationTest {

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line/{lineId}", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final LineResponse lineResponse = response.jsonPath().getObject("data.", LineResponse.class);

        assertThat(lineResponse.getLineDetail()).isEqualTo(new LineDetailResponse(1L, "2호선", "초록색"));
        assertThat(lineResponse.getStations()).containsExactly(
                new StationResponse(1L, "신림역"),
                new StationResponse(2L, "봉천역"),
                new StationResponse(3L, "서울대입구역"),
                new StationResponse(4L, "낙성대역"),
                new StationResponse(5L, "사당역"),
                new StationResponse(6L, "방배역"),
                new StationResponse(7L, "서초역")
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());


        List<LineResponse> result = response.jsonPath().getList("data.", LineResponse.class);

        assertAll(
                () -> assertThat(result.get(0).getLineDetail()).isEqualTo(new LineDetailResponse(1L, "2호선", "초록색")),
                () -> assertThat(result.get(0).getStations()).containsExactly(
                        new StationResponse(1L, "신림역"),
                        new StationResponse(2L, "봉천역"),
                        new StationResponse(3L, "서울대입구역"),
                        new StationResponse(4L, "낙성대역"),
                        new StationResponse(5L, "사당역"),
                        new StationResponse(6L, "방배역"),
                        new StationResponse(7L, "서초역")
                ),
                () -> assertThat(result.get(1).getLineDetail()).isEqualTo(new LineDetailResponse(2L, "3호선", "파란색")),
                () -> assertThat(result.get(1).getStations()).containsExactly(
                        new StationResponse(8L, "교대역"),
                        new StationResponse(9L, "강남역"),
                        new StationResponse(4L, "낙성대역"),
                        new StationResponse(10L, "역삼역"),
                        new StationResponse(11L, "선릉역")
                )
        );
    }

    @Test
    void 최단거리를_조회한다() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/line/path?startLineId=8&endLineId=7")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final ShortestPath shortestPath = response.jsonPath().getObject("data.", ShortestPath.class);

        Assertions.assertAll(
                () -> assertThat(shortestPath.getPath()).containsExactly(
                        new StationEntity(8L, "교대역"),
                        new StationEntity(9L, "강남역"),
                        new StationEntity(4L, "낙성대역"),
                        new StationEntity(5L, "사당역"),
                        new StationEntity(6L, "방배역"),
                        new StationEntity(7L, "서초역")
                ),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(35.0),
                () -> assertThat(shortestPath.getFare()).isEqualTo(1_750)
        );
    }
}
