package subway.adapter.in.web.route;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import subway.adapter.in.web.route.dto.FindShortCutRequest;
import subway.adapter.out.persistence.repository.LineJdbcRepository;
import subway.adapter.out.persistence.repository.SectionJdbcRepository;
import subway.adapter.out.persistence.repository.StationJdbcRepository;
import subway.application.dto.RouteResponse;
import subway.common.IntegrationTest;
import subway.domain.Fare;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RouteControllerTest extends IntegrationTest {

    @Autowired
    private LineJdbcRepository lineJdbcRepository;
    @Autowired
    private StationJdbcRepository stationJdbcRepository;
    @Autowired
    private SectionJdbcRepository sectionJdbcAdapter;

    @Test
    @DisplayName("GET /station/route 경로를 조회하면 최단 경로와 그 경로로 이동할때 발생하는 비용 테스트")
    void findResultShotCut() {
        Long line1Id = lineJdbcRepository.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcRepository.createLine(new Line("2호선", 10));

        stationJdbcRepository.createStation(new Station("가"));
        stationJdbcRepository.createStation(new Station("나"));
        stationJdbcRepository.createStation(new Station("다"));
        stationJdbcRepository.createStation(new Station("라"));
        stationJdbcRepository.createStation(new Station("마"));
        stationJdbcRepository.createStation(new Station("바"));
        stationJdbcRepository.createStation(new Station("사"));

        List<Section> line1Section = Arrays.asList(
                new Section(1L, new Station("가"), new Station("나"), 5L),
                new Section(1L, new Station("나"), new Station("다"), 5L),
                new Section(1L, new Station("다"), new Station("라"), 5L),
                new Section(1L, new Station("라"), new Station("마"), 5L),
                new Section(1L, new Station("마"), new Station("바"), 5L)
        );

        List<Section> line2Section = Arrays.asList(
                new Section(2L, new Station("다"), new Station("사"), 1L),
                new Section(2L, new Station("사"), new Station("라"), 1L)
        );

        sectionJdbcAdapter.saveSection(line1Id, line1Section);
        sectionJdbcAdapter.saveSection(line2Id, line2Section);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("fromStation", "가")
                .param("toStation", "라")
                .param("age", 5000)
                .when().get("/stations/route")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getObject("", RouteResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new RouteResponse(List.of("가", "나", "다", "사", "라"), new Fare(1350)))
        );
    }

    @Test
    @DisplayName("GET /station/route 나이의 따른 요금 테스트_청소년")
    void findResultShotCutByAge_teenager() {
        Long line1Id = lineJdbcRepository.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcRepository.createLine(new Line("2호선", 10));

        stationJdbcRepository.createStation(new Station("가"));
        stationJdbcRepository.createStation(new Station("나"));
        stationJdbcRepository.createStation(new Station("다"));
        stationJdbcRepository.createStation(new Station("라"));
        stationJdbcRepository.createStation(new Station("마"));
        stationJdbcRepository.createStation(new Station("바"));
        stationJdbcRepository.createStation(new Station("사"));

        List<Section> line1Section = Arrays.asList(
                new Section(1L, new Station("가"), new Station("나"), 5L),
                new Section(1L, new Station("나"), new Station("다"), 5L),
                new Section(1L, new Station("다"), new Station("라"), 5L),
                new Section(1L, new Station("라"), new Station("마"), 5L),
                new Section(1L, new Station("마"), new Station("바"), 5L)
        );

        List<Section> line2Section = Arrays.asList(
                new Section(2L, new Station("다"), new Station("사"), 1L),
                new Section(2L, new Station("사"), new Station("라"), 1L)
        );

        sectionJdbcAdapter.saveSection(line1Id, line1Section);
        sectionJdbcAdapter.saveSection(line2Id, line2Section);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("fromStation", "가")
                .param("toStation", "라")
                .param("age", 13)
                .when().get("/stations/route")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getObject("", RouteResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new RouteResponse(List.of("가", "나", "다", "사", "라"), new Fare(800)))
        );
    }

    @Test
    @DisplayName("GET /station/route 나이의 따른 요금 테스트_어린이")
    void findResultShotCutByAge_children() {
        Long line1Id = lineJdbcRepository.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcRepository.createLine(new Line("2호선", 10));

        stationJdbcRepository.createStation(new Station("가"));
        stationJdbcRepository.createStation(new Station("나"));
        stationJdbcRepository.createStation(new Station("다"));
        stationJdbcRepository.createStation(new Station("라"));
        stationJdbcRepository.createStation(new Station("마"));
        stationJdbcRepository.createStation(new Station("바"));
        stationJdbcRepository.createStation(new Station("사"));

        List<Section> line1Section = Arrays.asList(
                new Section(1L, new Station("가"), new Station("나"), 5L),
                new Section(1L, new Station("나"), new Station("다"), 5L),
                new Section(1L, new Station("다"), new Station("라"), 5L),
                new Section(1L, new Station("라"), new Station("마"), 5L),
                new Section(1L, new Station("마"), new Station("바"), 5L)
        );

        List<Section> line2Section = Arrays.asList(
                new Section(2L, new Station("다"), new Station("사"), 1L),
                new Section(2L, new Station("사"), new Station("라"), 1L)
        );

        sectionJdbcAdapter.saveSection(line1Id, line1Section);
        sectionJdbcAdapter.saveSection(line2Id, line2Section);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("fromStation", "가")
                .param("toStation", "라")
                .param("age", 7)
                .when().get("/stations/route")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getObject("", RouteResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new RouteResponse(List.of("가", "나", "다", "사", "라"), new Fare(500)))
        );
    }

    @Test
    @DisplayName("GET /station/route 노선별 추가 요금 테스트")
    void findResultShotCutByLineSurCharge() {
        Long line1Id = lineJdbcRepository.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcRepository.createLine(new Line("2호선", 2000));

        stationJdbcRepository.createStation(new Station("가"));
        stationJdbcRepository.createStation(new Station("나"));
        stationJdbcRepository.createStation(new Station("다"));
        stationJdbcRepository.createStation(new Station("라"));
        stationJdbcRepository.createStation(new Station("마"));
        stationJdbcRepository.createStation(new Station("바"));
        stationJdbcRepository.createStation(new Station("사"));

        List<Section> line1Section = Arrays.asList(
                new Section(1L, new Station("가"), new Station("나"), 5L),
                new Section(1L, new Station("나"), new Station("다"), 5L),
                new Section(1L, new Station("다"), new Station("라"), 5L),
                new Section(1L, new Station("라"), new Station("마"), 5L),
                new Section(1L, new Station("마"), new Station("바"), 5L)
        );

        List<Section> line2Section = Arrays.asList(
                new Section(2L, new Station("다"), new Station("사"), 1L),
                new Section(2L, new Station("사"), new Station("라"), 1L)
        );

        sectionJdbcAdapter.saveSection(line1Id, line1Section);
        sectionJdbcAdapter.saveSection(line2Id, line2Section);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("fromStation", "가")
                .param("toStation", "라")
                .param("age", 27)
                .when().get("/stations/route")
                .then().log().all()
                .extract();

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getObject("", RouteResponse.class))
                        .usingRecursiveComparison()
                        .isEqualTo(new RouteResponse(List.of("가", "나", "다", "사", "라"), new Fare(3250)))
        );
    }
}