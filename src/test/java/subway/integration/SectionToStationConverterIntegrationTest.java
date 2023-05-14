package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dto.LineResponse;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionDeleteRequest;
import subway.section.dto.SectionResponse;
import subway.station.domain.Station;
import subway.subwayMap.dto.SubwayMapForLineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionToStationConverterIntegrationTest extends IntegrationTest{

    @Test
    void addSectionTest() {
        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest(1L, 7L, 8L, true, 3);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)
                .when().post("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList(".", SectionResponse.class);

        // when
        final ExtractableResponse<Response> response1 = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/subway-map/{lineId}", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());
        final SubwayMapForLineResponse subwayMapForLineResponse = response1.as(SubwayMapForLineResponse.class);
        assertThat(subwayMapForLineResponse.getLineResponse()).isEqualTo(new LineResponse(1L, "2호선", "초록색"));
        assertThat(subwayMapForLineResponse.getStations()).containsExactly(
                new Station(1L, "신림역"),
                new Station(2L, "봉천역"),
                new Station(3L, "서울대입구역"),
                new Station(4L, "낙성대역"),
                new Station(5L, "사당역"),
                new Station(6L, "방배역"),
                new Station(7L, "서초역"),
                new Station(8L, "교대역")
        );
    }

    @Test
    void deleteSectionTest() {
        // given
        final SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest(1L, 3L);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionDeleteRequest)
                .when().delete("/sections")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // when
        final ExtractableResponse<Response> response1 = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/subway-map/{lineId}", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());
        final SubwayMapForLineResponse subwayMapForLineResponse = response1.as(SubwayMapForLineResponse.class);
        assertThat(subwayMapForLineResponse.getLineResponse()).isEqualTo(new LineResponse(1L, "2호선", "초록색"));
        assertThat(subwayMapForLineResponse.getStations()).containsExactly(
                new Station(1L, "신림역"),
                new Station(2L, "봉천역"),
                new Station(4L, "낙성대역"),
                new Station(5L, "사당역"),
                new Station(6L, "방배역"),
                new Station(7L, "서초역")
        );
    }
}
