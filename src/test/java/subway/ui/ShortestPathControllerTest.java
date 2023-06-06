package subway.ui;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Distance;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import javax.validation.Valid;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("classpath:/remove-station.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortestPathControllerTest {

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Autowired
    SectionDao sectionDao;

    private Long station1Id;
    private Long station3Id;

    @BeforeEach
    void init(@LocalServerPort int port) {
        RestAssured.port = port;
        init();
    }

    private void init() {
        Station station1 = new Station("잠실역1");
        Station station2 = new Station("잠실역2");
        Station station3 = new Station("잠실역3");
        Station station4 = new Station("잠실역4");

        Station getStation1 = stationDao.insert(station1);
        Station getStation2 = stationDao.insert(station2);
        Station getStation3 = stationDao.insert(station3);
        Station getStation4 = stationDao.insert(station4);
        station1Id = getStation1.getId();
        station3Id = getStation3.getId();

        Long 일호선 = lineDao.insert(new Line("1호선", "초록색"));
        Long 이호선 = lineDao.insert(new Line("2호선", "노랑색"));

        sectionDao.insert(new Section(new Distance(10), getStation1, getStation2, 일호선));
        sectionDao.insert(new Section(new Distance(20), getStation2, getStation3, 일호선));
        sectionDao.insert(new Section(new Distance(10), getStation1, getStation4, 이호선));
        sectionDao.insert(new Section(new Distance(30), getStation4, getStation3, 이호선));
    }

    @Test
    void 최소경로를_가져온다() {
        PathRequest pathRequest = new PathRequest(station1Id, station3Id);

        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().get("/shortest-path")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
        final JsonPath result = response.jsonPath();

        assertAll(
                () -> assertThat(result.getInt("distance")).isEqualTo(30),
                () -> assertThat(result.getInt("fee")).isEqualTo(1650)
        );
    }
}
