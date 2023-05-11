package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class RouteDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private RouteDao routeDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        routeDao = new RouteDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    void DB_데이터를_통해_Route_객체를_생성한다() {
        // given
        final List<Line> lines = lineDao.findAll();
        final Route route = routeDao.findRoute(lines);
        // expect
        assertSoftly(softly -> {
            final Map<Line, Sections> sectionsByLine = route.getSectionsByLine();
            softly.assertThat(sectionsByLine.get(lines.get(0)))
                    .isEqualTo(new Sections(List.of(
                            new Section(
                                    new Station(1L, "후추"),
                                    new Station(2L, "디노"),
                                    5))));
        });
    }

}
