package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.path.PathStrategy;
import subway.domain.path.ShortestDistancePathStrategy;
import subway.domain.subway.Distance;
import subway.domain.subway.Line;
import subway.domain.subway.Section;
import subway.domain.subway.Station;

import javax.sql.DataSource;

@Sql("classpath:/remove-section-line.sql")
@JdbcTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private PathService pathService;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private PathStrategy pathStrategy;

    // 1 -> 2 -> 3  (10,20)
    // 1-> 4 -> 3  (10,10)
    @BeforeEach
    void init() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        pathStrategy = new ShortestDistancePathStrategy();
        pathService = new PathService(sectionDao, pathStrategy);
        initialSection();
    }

    private void initialSection() {
        Long lineId2 = lineDao.insert(new Line("2호선", "초록색"));
        Long lineId3 = lineDao.insert(new Line("3호선", "노랑색"));

        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        Station station3 = new Station(3L, "잠실역3");
        Station station4 = new Station(4L, "잠실역4");

        stationDao.insert(station1).getId();
        stationDao.insert(station2).getId();
        stationDao.insert(station3).getId();
        stationDao.insert(station4).getId();

        sectionDao.insert(new Section(new Distance(10), station1, station2, lineId2));
        sectionDao.insert(new Section(new Distance(20), station2, station3, lineId2));
        sectionDao.insert(new Section(new Distance(10), station1, station4, lineId3));
        sectionDao.insert(new Section(new Distance(10), station4, station3, lineId3));
    }
}
