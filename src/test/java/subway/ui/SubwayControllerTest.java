package subway.ui;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.integration.IntegrationTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SubwayControllerTest extends IntegrationTest {
    @Autowired
    private StationDao stationDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @DisplayName("두 역사이의 최단 경로를 반환받는다.")
    @Test
    void findPathBetween() {
        //given
        sectionSetting();

        //when
        final PathResponse pathResponse = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("from", 1)
                .queryParam("to", 5)
                .when().get("/subway/paths")
                .then()
                .extract()
                .as(PathResponse.class);

        final int expectPare = 1250 + 800 + 300;


        //then
        assertAll(
                () -> assertThat(pathResponse.getPaths()).containsExactly(
                        StationResponse.of(new Station(1L, "수성")),
                        StationResponse.of(new Station(2L, "금성")),
                        StationResponse.of(new Station(3L, "지구")),
                        StationResponse.of(new Station(5L, "잠실"))
                ),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(70),
                () -> assertThat(pathResponse.getFare()).isEqualTo(expectPare)
        );
    }

    private void sectionSetting() {
        prepareStation();
        prepareLine();
        prepareSection();
    }

    private void prepareStation() {
        stationDao.insert(new StationEntity(1L, "수성"));
        stationDao.insert(new StationEntity(2L, "금성"));
        stationDao.insert(new StationEntity(3L, "지구"));
        stationDao.insert(new StationEntity(4L, "화성"));
        stationDao.insert(new StationEntity(5L, "잠실"));
        stationDao.insert(new StationEntity(6L, "집"));
    }

    private void prepareLine() {
        lineDao.insert(new LineEntity(1L, "1호선", "red"));
        lineDao.insert(new LineEntity(2L, "2호선", "blue"));
    }

    private void prepareSection() {
        sectionDao.save(List.of(
                new SectionEntity(1L, "수성", "금성", 10),
                new SectionEntity(1L, "금성", "지구", 20),
                new SectionEntity(1L, "지구", "화성", 30)
        ));

        sectionDao.save(List.of(
                new SectionEntity(2L, "지구", "잠실", 40)
        ));
    }

    @DisplayName("갈 수 없는 역으로의 요청을 보낸다면 예외를 바생시킨다.")
    @Test
    void findPathBetween_invalidPath() {
        //given
        sectionSetting();

        //when
        final String errorMessage = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("from", 1)
                .queryParam("to", 6)
                .when().get("/subway/paths")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .asString();

        //then
        assertThat(errorMessage).isEqualTo("수성에서 집으로 갈 수 없습니다.");
    }
}
