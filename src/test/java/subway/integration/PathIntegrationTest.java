package subway.integration;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.ui.dto.request.PathRequest;

public class PathIntegrationTest extends IntegrationTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @BeforeEach
    public void setUp() {
        super.setUp();

        LineEntity lineEntity1 = new LineEntity(1L, "2호선");
        LineEntity lineEntity2 = new LineEntity(2L, "8호선");
        lineDao.save(lineEntity1);
        lineDao.save(lineEntity2);

        StationEntity stationEntity1 = new StationEntity(1L, "강남역");
        StationEntity stationEntity2 = new StationEntity(2L, "삼성역");
        StationEntity stationEntity3 = new StationEntity(3L, "잠실역");
        StationEntity stationEntity4 = new StationEntity(4L, "몽촌토성역");
        stationDao.save(stationEntity1);
        stationDao.save(stationEntity2);
        stationDao.save(stationEntity3);
        stationDao.save(stationEntity4);

        SectionEntity sectionEntity1 = new SectionEntity(1L, lineEntity1.getId(), stationEntity1.getId(),
                stationEntity2.getId(), 5);
        SectionEntity sectionEntity2 = new SectionEntity(2L, lineEntity1.getId(), stationEntity2.getId(),
                stationEntity3.getId(), 4);
        SectionEntity sectionEntity3 = new SectionEntity(3L, lineEntity2.getId(), stationEntity4.getId(),
                stationEntity3.getId(), 7);

        sectionDao.save(sectionEntity1);
        sectionDao.save(sectionEntity2);
        sectionDao.save(sectionEntity3);
    }

    @DisplayName("역과 역 사이의 최단 경로를 조회한다.")
    @Test
    void findPath() {
        // given
        PathRequest pathRequest = new PathRequest("강남역", "잠실역");

        // when, then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().get("/path")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("fare", equalTo(1_250))
                .body("distance", equalTo(9))
                .rootPath("stations")
                .body("[0].stationName", equalTo("강남역"))
                .body("[1].stationName", equalTo("삼성역"))
                .body("[2].stationName", equalTo("잠실역"));
    }

    @DisplayName("다른 호선에 있는 역들의 최단 경로를 조회한다.")
    @Test
    void findPathByDifferentLine() {
        // given
        PathRequest pathRequest = new PathRequest("몽촌토성역", "삼성역");

        // when, then
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().get("/path")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("fare", equalTo(1_350))
                .body("distance", equalTo(11))
                .rootPath("stations")
                .body("[0].stationName", equalTo("몽촌토성역"))
                .body("[1].stationName", equalTo("잠실역"))
                .body("[2].stationName", equalTo("삼성역"));
    }
}
