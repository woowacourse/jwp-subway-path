package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.SectionResponse;
import subway.dto.StationRegisterRequest;
import subway.entity.StationEntity;
import subway.service.LineService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
public class LineIntegrationTest extends IntegrationTest {

    @Autowired
    private LineService lineService;

    private LineRequest lineRequest1;
    private LineRequest lineRequest2;

    @Override
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
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/line")
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
                .when().post("/line")
                .then().log().all().
                extract();

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/line")
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/line")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest2)
                .when().put("/line/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        final ExtractableResponse<Response> createResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineRequest1)
                .when().post("/line")
                .then().log().all().
                extract();

        // when
        final Long lineId = Long.parseLong(createResponse.header("Location").split("/")[2]);
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/line/{lineId}", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에 역을 등록한다.")
    @Test
    void createSection() {
        StationRegisterRequest stationRegisterRequest = new StationRegisterRequest(8L, 2L, 3);

        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationRegisterRequest)
                .when().post("/line/{lineId}", 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        final List<SectionResponse> result = response.jsonPath().getList("data.", SectionResponse.class);
        Assertions.assertAll(
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getUpStationId()).isEqualTo(8L),
                () -> assertThat(result.get(0).getDownStationId()).isEqualTo(2L),
                () -> assertThat(result.get(0).getDistance()).isEqualTo(3)
        );
    }

    @DisplayName("노선에서 첫번째 역을 삭제한다")
    @Test
    void 노선에서_첫번째_역을_삭제한다() {
        // when
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/line/{lineId}/station/{stationId}", 1L, 1L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 한_라인_노선도_조회() {
        Line line = lineService.findLineById(1L);

        List<StationEntity> stations = line.getStations();

        assertAll(
                () -> org.assertj.core.api.Assertions.assertThat(stations).containsExactly(
                        new StationEntity(1L, "신림역"),
                        new StationEntity(2L, "봉천역"),
                        new StationEntity(3L, "서울대입구역"),
                        new StationEntity(4L, "낙성대역"),
                        new StationEntity(5L, "사당역"),
                        new StationEntity(6L, "방배역"),
                        new StationEntity(7L, "서초역")
                ),
                () -> org.assertj.core.api.Assertions.assertThat(line.getId()).isEqualTo(1L)
        );
    }
}
