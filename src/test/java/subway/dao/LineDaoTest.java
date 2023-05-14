package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.application.SectionService;
import subway.domain.Line;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({StationDao.class, LineDao.class, SectionService.class, SectionDao.class})
class LineDaoTest {
    @Autowired
    LineDao lineDao;

    @Autowired
    StationDao stationDao;

    @Autowired
    SectionService sectionService;

    @Test
    @DisplayName("어떤 노선의 상행 종점역을 찾을 수 있다.")
    void head() {
        // given
        final var line = lineDao.insert(new Line("1호선", "blue"));
        final var stationS = stationDao.insert(new Station("송탄"));

        lineDao.updateHeadStation(line, stationS);

        // when
        final var updatedLine = lineDao.findById(line.getId());
        assertThat(updatedLine.getHead()).isEqualTo(stationS);
    }

    @Test
    @DisplayName("어떤 노선에 상행 종점역이 등록되어 있다면, 노선 조회시 해당 역 정보가 함께 들어있다.")
    void findHead() {
        // given
        final var line = lineDao.insert(new Line("1호선", "blue"));
        final var stationS = stationDao.insert(new Station("송탄"));
        lineDao.updateHeadStation(line, stationS);

        // when
        final var foundLine = lineDao.findById(line.getId());
        assertThat(foundLine.getHead()).isEqualTo(stationS);
    }

    @Test
    @DisplayName("어떤 노선에 아직 상행 종점역이 등록되지 않은 상태라면, 노선 조회시 역 정보는 null로 지정되어 있다.")
    void notFoundHead() {
        // given
        final var line = lineDao.insert(new Line("1호선", "blue"));

        // when
        final var foundLine = lineDao.findById(line.getId());
        assertThat(foundLine.getHead()).isNull();
    }
}