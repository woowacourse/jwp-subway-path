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
        final var headStation = lineDao.findHeadStation(line);
        assertThat(headStation).isEqualTo(stationS);
    }
}