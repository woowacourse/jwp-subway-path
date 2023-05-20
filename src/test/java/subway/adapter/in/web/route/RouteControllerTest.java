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
import subway.adapter.out.persistence.repository.LineJdbcAdapter;
import subway.adapter.out.persistence.repository.SectionJdbcAdapter;
import subway.adapter.out.persistence.repository.StationJdbcAdapter;
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
    private LineJdbcAdapter lineJdbcAdapter;
    @Autowired
    private StationJdbcAdapter stationJdbcAdapter;
    @Autowired
    private SectionJdbcAdapter sectionJdbcAdapter;

    @Test
    @DisplayName("GET /station/route 경로를 조회하면 최단 경로와 그 경로로 이동할때 발생하는 비용 테스트")
    void findResultShotCut() {
        Long line1Id = lineJdbcAdapter.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcAdapter.createLine(new Line("2호선", 10));

        stationJdbcAdapter.createStation(new Station("가"));
        stationJdbcAdapter.createStation(new Station("나"));
        stationJdbcAdapter.createStation(new Station("다"));
        stationJdbcAdapter.createStation(new Station("라"));
        stationJdbcAdapter.createStation(new Station("마"));
        stationJdbcAdapter.createStation(new Station("바"));
        stationJdbcAdapter.createStation(new Station("사"));

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
                .body(new FindShortCutRequest("가", "라", 5000))
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
        Long line1Id = lineJdbcAdapter.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcAdapter.createLine(new Line("2호선", 10));

        stationJdbcAdapter.createStation(new Station("가"));
        stationJdbcAdapter.createStation(new Station("나"));
        stationJdbcAdapter.createStation(new Station("다"));
        stationJdbcAdapter.createStation(new Station("라"));
        stationJdbcAdapter.createStation(new Station("마"));
        stationJdbcAdapter.createStation(new Station("바"));
        stationJdbcAdapter.createStation(new Station("사"));

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
                .body(new FindShortCutRequest("가", "라", 13))
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
        Long line1Id = lineJdbcAdapter.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcAdapter.createLine(new Line("2호선", 10));

        stationJdbcAdapter.createStation(new Station("가"));
        stationJdbcAdapter.createStation(new Station("나"));
        stationJdbcAdapter.createStation(new Station("다"));
        stationJdbcAdapter.createStation(new Station("라"));
        stationJdbcAdapter.createStation(new Station("마"));
        stationJdbcAdapter.createStation(new Station("바"));
        stationJdbcAdapter.createStation(new Station("사"));

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
                .body(new FindShortCutRequest("가", "라", 7))
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
        Long line1Id = lineJdbcAdapter.createLine(new Line("1호선", 100));
        Long line2Id = lineJdbcAdapter.createLine(new Line("2호선", 2000));

        stationJdbcAdapter.createStation(new Station("가"));
        stationJdbcAdapter.createStation(new Station("나"));
        stationJdbcAdapter.createStation(new Station("다"));
        stationJdbcAdapter.createStation(new Station("라"));
        stationJdbcAdapter.createStation(new Station("마"));
        stationJdbcAdapter.createStation(new Station("바"));
        stationJdbcAdapter.createStation(new Station("사"));

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
                .body(new FindShortCutRequest("가", "라", 27))
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