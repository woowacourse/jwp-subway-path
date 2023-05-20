package subway.integration;

import static org.hamcrest.Matchers.equalTo;
import static subway.fixture.LineFixture.사호선;
import static subway.fixture.LineFixture.삼호선;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.LineFixture.일호선;
import static subway.fixture.SectionFixture.강남역_용산역_10;
import static subway.fixture.SectionFixture.남영역_서울역_10;
import static subway.fixture.SectionFixture.사당역_혜화역_10;
import static subway.fixture.SectionFixture.서울역_시청역_5;
import static subway.fixture.SectionFixture.서울역_용산역_5;
import static subway.fixture.SectionFixture.시청역_선릉역_5;
import static subway.fixture.SectionFixture.시청역_종각역_10;
import static subway.fixture.SectionFixture.신사역_수서역_7;
import static subway.fixture.SectionFixture.용산역_사당역_10;
import static subway.fixture.SectionFixture.용산역_시청역_5;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.남영역;
import static subway.fixture.StationFixture.사당역;
import static subway.fixture.StationFixture.서울역;
import static subway.fixture.StationFixture.서초역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.수서역;
import static subway.fixture.StationFixture.시청역;
import static subway.fixture.StationFixture.신사역;
import static subway.fixture.StationFixture.역삼역;
import static subway.fixture.StationFixture.용산역;
import static subway.fixture.StationFixture.종각역;
import static subway.fixture.StationFixture.혜화역;
import static subway.fixture.StationFixture.회현역;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.SectionEntity;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Sql("/truncate.sql")
@DisplayName("최단 경로 찾기 기능")
@SuppressWarnings("NonAsciiCharacters")
public class SubwayIntegrationTest extends IntegrationTest {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @BeforeEach
    void setUpLineAndStation() {
        stationRepository.save(남영역);
        stationRepository.save(서울역);
        stationRepository.save(시청역);
        stationRepository.save(종각역);
        stationRepository.save(서초역);
        stationRepository.save(강남역);
        stationRepository.save(역삼역);
        stationRepository.save(선릉역);
        stationRepository.save(사당역);
        stationRepository.save(용산역);
        stationRepository.save(혜화역);
        stationRepository.save(회현역);
        stationRepository.save(신사역);
        stationRepository.save(수서역);
        lineRepository.save(일호선);
        lineRepository.save(이호선);
        lineRepository.save(삼호선);
        lineRepository.save(사호선);
        lineRepository.saveSection(
                new SectionEntity(null, 일호선.getId(), 남영역_서울역_10.getLeft().getId(), 남영역_서울역_10.getRight().getId(),
                        남영역_서울역_10.getDistance()));
        lineRepository.saveSection(
                new SectionEntity(null, 일호선.getId(), 서울역_시청역_5.getLeft().getId(), 서울역_시청역_5.getRight().getId(),
                        서울역_시청역_5.getDistance()));
        lineRepository.saveSection(
                new SectionEntity(null, 일호선.getId(), 시청역_종각역_10.getLeft().getId(), 시청역_종각역_10.getRight().getId(),
                        시청역_종각역_10.getDistance()));

        lineRepository.saveSection(
                new SectionEntity(null, 이호선.getId(), 강남역_용산역_10.getLeft().getId(), 강남역_용산역_10.getRight().getId(),
                        강남역_용산역_10.getDistance()));
        lineRepository.saveSection(
                new SectionEntity(null, 이호선.getId(), 용산역_시청역_5.getLeft().getId(), 용산역_시청역_5.getRight().getId(),
                        용산역_시청역_5.getDistance()));
        lineRepository.saveSection(
                new SectionEntity(null, 이호선.getId(), 시청역_선릉역_5.getLeft().getId(), 시청역_선릉역_5.getRight().getId(),
                        시청역_선릉역_5.getDistance()));

        lineRepository.saveSection(
                new SectionEntity(null, 삼호선.getId(), 신사역_수서역_7.getLeft().getId(), 신사역_수서역_7.getRight().getId(),
                        신사역_수서역_7.getDistance()));

        lineRepository.saveSection(
                new SectionEntity(null, 사호선.getId(), 서울역_용산역_5.getLeft().getId(), 서울역_용산역_5.getRight().getId(),
                        서울역_용산역_5.getDistance()));
        lineRepository.saveSection(
                new SectionEntity(null, 사호선.getId(), 용산역_사당역_10.getLeft().getId(), 용산역_사당역_10.getRight().getId(),
                        용산역_사당역_10.getDistance()));
        lineRepository.saveSection(
                new SectionEntity(null, 사호선.getId(), 사당역_혜화역_10.getLeft().getId(), 사당역_혜화역_10.getRight().getId(),
                        사당역_혜화역_10.getDistance()));
    }

    @Test
    @DisplayName("최단경로 찾기 요청에 대한 응답을 반환한다.")
    void findShortestRoute() {
        String startStation = "강남역";
        String endStation = "선릉역";

        RestAssured.given().log().all()
                .queryParam("startStation", startStation)
                .queryParam("endStation", endStation)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/subway/shortest-route")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("distance", equalTo(20))
                .body("fare", equalTo(1450))
                .rootPath("routes")
                .body("[0].id", equalTo(강남역.getId().intValue()))
                .body("[0].name", equalTo(강남역.getName()))
                .body("[1].id", equalTo(용산역.getId().intValue()))
                .body("[1].name", equalTo(용산역.getName()))
                .body("[2].id", equalTo(시청역.getId().intValue()))
                .body("[2].name", equalTo(시청역.getName()))
                .body("[3].id", equalTo(선릉역.getId().intValue()))
                .body("[3].name", equalTo(선릉역.getName()));
    }

    @Test
    @DisplayName("최단경로 찾기 요청 결과가 없는 경우 응답을 반환한다.")
    void findShortestRouteFail() {
        String startStation = "강남역";
        String endStation = "신사역";

        RestAssured.given().log().all()
                .queryParam("startStation", startStation)
                .queryParam("endStation", endStation)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/subway/shortest-route")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo(startStation + "과 " + endStation + " 사이의 경로가 존재하지 않습니다."));
    }

    @Test
    @DisplayName("출발역이 존재하지 않는 경우 응답을 반환한다.")
    void findShortestRouteFailWithNoStartStation() {
        String startStation = "부산역";
        String endStation = "신사역";

        RestAssured.given().log().all()
                .queryParam("startStation", startStation)
                .queryParam("endStation", endStation)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/subway/shortest-route")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("존재하지 않는 역입니다."));
    }

    @Test
    @DisplayName("도착역이 존재하지 않는 경우 응답을 반환한다.")
    void findShortestRouteFailWithNoEndStation() {
        String startStation = "강남역";
        String endStation = "부산역";

        RestAssured.given().log().all()
                .queryParam("startStation", startStation)
                .queryParam("endStation", endStation)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/subway/shortest-route")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("존재하지 않는 역입니다."));
    }
}
