package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.line.dto.LineResponse;
import subway.domain.station.domain.Station;
import subway.domain.subwayMap.dto.SubwayMapForLineResponse;
import subway.domain.subwayMap.dto.SubwayMapResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 지도 관련 기능")
class SubwayMapIntegrationTest extends IntegrationTest {

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/subway-map/{lineId}", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final SubwayMapForLineResponse subwayMapForLineResponse = response.jsonPath().getObject("data.", SubwayMapForLineResponse.class);
        assertThat(subwayMapForLineResponse.getLineResponse()).isEqualTo(new LineResponse(1L, "2호선", "초록색"));
        assertThat(subwayMapForLineResponse.getStations()).containsExactly(
                new Station(1L, "신림역"),
                new Station(2L, "봉천역"),
                new Station(3L, "서울대입구역"),
                new Station(4L, "낙성대역"),
                new Station(5L, "사당역"),
                new Station(6L, "방배역"),
                new Station(7L, "서초역")
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/subway-map")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final SubwayMapResponse subwayMapResponse = new SubwayMapResponse(response.jsonPath().getList("data.subwayMapResponses.", SubwayMapForLineResponse.class));

        List<SubwayMapForLineResponse> result = subwayMapResponse.getSubwayMapResponses();

        assertAll(
                () -> assertThat(result.get(0).getLineResponse()).isEqualTo(new LineResponse(1L, "2호선", "초록색")),
                () -> assertThat(result.get(0).getStations()).containsExactly(
                        new Station(1L, "신림역"),
                        new Station(2L, "봉천역"),
                        new Station(3L, "서울대입구역"),
                        new Station(4L, "낙성대역"),
                        new Station(5L, "사당역"),
                        new Station(6L, "방배역"),
                        new Station(7L, "서초역")
                ),
                () -> assertThat(result.get(1).getLineResponse()).isEqualTo(new LineResponse(2L, "3호선", "파란색")),
                () -> assertThat(result.get(1).getStations()).containsExactly(
                        new Station(8L, "교대역"),
                        new Station(9L, "강남역"),
                        new Station(4L, "낙성대역"),
                        new Station(10L, "역삼역"),
                        new Station(11L, "선릉역")
                )
        );
    }

}
