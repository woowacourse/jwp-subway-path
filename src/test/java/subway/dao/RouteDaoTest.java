package subway.dao;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;
import subway.domain.Route;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test_data.sql")
class RouteDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private RouteDao routeDao;
    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        routeDao = new RouteDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    void DB_데이터를_통해_Route_객체를_생성한다() {
        // given
        final Station 후추 = stationDao.insert(new Station("후추"));
        final Station 디노 = stationDao.insert(new Station("디노"));
        final Line lineNumber2 = lineDao.insert(new Line("2호선", "green"));
        sectionDao.insert(후추.getId(), 디노.getId(), 5, lineNumber2.getId());
        // when
        final List<Line> lines = lineDao.findAll();
        final Route route = routeDao.findRoute(lines);
        // then
        assertSoftly(softly -> {
            final Map<Line, Sections> sectionsByLine = route.getSectionsByLine();
            softly.assertThat(sectionsByLine.get(lines.get(0)))
                    .isEqualTo(new Sections(List.of(
                            new Section(
                                    lineNumber2.getId(), new Station(1L, "후추"),
                                    new Station(2L, "디노"),
                                    5))));
        });
    }

}
