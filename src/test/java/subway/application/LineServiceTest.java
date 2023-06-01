package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.subway.Distance;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.LineRequest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("classpath:/remove-section-line.sql")
@JdbcTest
class LineServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private LineService lineService;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;

    @BeforeEach
    void init() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        lineService = new LineService(lineDao, sectionDao, stationDao);
        initialSection();
    }

    private void initialSection() {
        Long lineId = lineDao.insert(new Line("2호선", "초록색"));

        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        Station station3 = new Station(3L, "잠실역3");
        Station station4 = new Station(4L, "잠실역4");

        stationDao.insert(station1).getId();
        stationDao.insert(station2).getId();
        stationDao.insert(station3).getId();
        stationDao.insert(station4).getId();

        sectionDao.insert(new Section(new Distance(10), station1, station2, lineId));
        sectionDao.insert(new Section(new Distance(20), station2, station3, lineId));
    }

    @Test
    void 라인을_저장한다() {
        LineRequest lineRequest = new LineRequest("삼호선", "노랑색", 1L, 2L, 3);
        assertThat(lineService.saveLine(lineRequest)).isEqualTo(2L);
    }

    @Test
    void 모든_라인을_반환한다() {
        //given
        LineRequest lineRequest = new LineRequest("삼호선", "노랑색", 1L, 2L, 3);
        lineService.saveLine(lineRequest);

        // when & then
        lineService.findLineResponses();
        assertThat(lineService.findLineResponses()).hasSize(2);
    }

    @Test
    void 아이디로_라인을_반환한다() {
        LineRequest lineRequest = new LineRequest("삼호선", "노랑색", 1L, 2L, 3);
        Long lineId = lineService.saveLine(lineRequest);

        assertThat(lineId).isEqualTo(lineService.findLineResponseById(lineId).getId());
    }

    @Test
    void 아이디로_라인을_삭제한다() {
        LineRequest lineRequest = new LineRequest("삼호선", "노랑색", 1L, 2L, 3);
        Long lineId = lineService.saveLine(lineRequest);

        lineService.deleteLineById(lineId);
        assertThatThrownBy(
                () -> assertThat(lineService.findLineById(lineId)).isNull()
        );
    }
}
