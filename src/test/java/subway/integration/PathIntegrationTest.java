package subway.integration;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.LineRequest;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.entity.LineEntity;
import subway.entity.StationEntity;
import subway.service.PathService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PathIntegrationTest extends IntegrationTest {

    @Autowired
    private PathService pathService;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private LineDao lineDao;

    @DisplayName("최단 거리의 경로를 구한다")
    @Test
    void findShortestPath() {
        //given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", 3L, 2, 1L);
        StationEntity thirdStation = new StationEntity(3L, "강남역", null, 0, 1L);
        stationDao.insert(station);
        stationDao.insert(nextStation);
        stationDao.insert(thirdStation);

        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineDao.insert(new LineEntity(1L, lineRequest1.getName(), lineRequest1.getColor(), 1L));

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ShortestPathRequest("역삼역", "강남역"))
                .when().get("/shortestPath")
                .then().log().all()
                .extract();

        ShortestPathResponse shortestPath = pathService.findShortestPath(new ShortestPathRequest("역삼역", "강남역"));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(shortestPath.getPath()).isEqualTo(List.of("역삼역", "선릉역", "강남역"));
    }

    @DisplayName("최단 거리를 구한다")
    @Test
    void findDistance() {
        //given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", 3L, 2, 1L);
        StationEntity thirdStation = new StationEntity(3L, "강남역", null, 0, 1L);
        stationDao.insert(station);
        stationDao.insert(nextStation);
        stationDao.insert(thirdStation);

        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineDao.insert(new LineEntity(1L, lineRequest1.getName(), lineRequest1.getColor(), 1L));

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ShortestPathRequest("역삼역", "강남역"))
                .when().get("/shortestPath")
                .then().log().all()
                .extract();

        ShortestPathResponse shortestPath = pathService.findShortestPath(new ShortestPathRequest("역삼역", "강남역"));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(shortestPath.getDistance()).isEqualTo(12);
    }

    @DisplayName("최단 거리의 요금을 구한다")
    @Test
    void findCost() {
        //given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "선릉역", 3L, 2, 1L);
        StationEntity thirdStation = new StationEntity(3L, "강남역", null, 0, 1L);
        stationDao.insert(station);
        stationDao.insert(nextStation);
        stationDao.insert(thirdStation);

        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600");
        lineDao.insert(new LineEntity(1L, lineRequest1.getName(), lineRequest1.getColor(), 1L));

        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ShortestPathRequest("역삼역", "강남역"))
                .when().get("/shortestPath")
                .then().log().all()
                .extract();

        ShortestPathResponse shortestPath = pathService.findShortestPath(new ShortestPathRequest("역삼역", "강남역"));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(shortestPath.getCost()).isEqualTo(1350);
    }

    @DisplayName("환승을 고려하여 최단경로를 구한다")
    @Test
    void findShortestPath_all_line() {
        //given
        StationEntity station = new StationEntity(1L, "역삼역", 2L, 10, 1L);
        StationEntity nextStation = new StationEntity(2L, "신림역", 3L, 10, 1L);
        StationEntity thirdStation = new StationEntity(3L, "선릉역", 4L, 2, 1L);
        StationEntity fourthStation = new StationEntity(4L, "강남역", null, 0, 1L);
        stationDao.insert(station);
        stationDao.insert(nextStation);
        stationDao.insert(thirdStation);
        stationDao.insert(fourthStation);

        StationEntity otherLineStation = new StationEntity(5L, "신림역", 6L, 1, 2L);
        StationEntity otherLineStationNextStation = new StationEntity(6L, "환승역", 7L, 2, 2L);
        StationEntity otherLineStationThirdStation = new StationEntity(7L, "삼성역", null, 0, 2L);
        stationDao.insert(otherLineStation);
        stationDao.insert(otherLineStationNextStation);
        stationDao.insert(otherLineStationThirdStation);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600");
        lineDao.insert(new LineEntity(1L, lineRequest.getName(), lineRequest.getColor(), 1L));

        LineRequest otherLineRequest = new LineRequest("두번째노선", "bg-orange-500");
        lineDao.insert(new LineEntity(2L, otherLineRequest.getName(), otherLineRequest.getColor(), 5L));


        //when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ShortestPathRequest("역삼역", "환승역"))
                .when().get("/shortestPath")
                .then().log().all()
                .extract();

        ShortestPathResponse shortestPath = pathService.findShortestPath(new ShortestPathRequest("역삼역", "환승역"));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(shortestPath.getDistance()).isEqualTo(11);
    }
}
