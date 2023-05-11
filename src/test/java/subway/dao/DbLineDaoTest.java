package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.LINE_999;
import static subway.fixture.StationFixture.EXPRESS_BUS_TERMINAL_STATION;
import static subway.fixture.StationFixture.SAPYEONG_STATION;

@JdbcTest
class DbLineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        lineDao = new DbLineDao(jdbcTemplate);
        stationDao = new DbStationDao(jdbcTemplate, dataSource);
    }

    @Test
    void saveLineEntitiesTest() {
        final LineEntity lineEntity = new LineEntity("1호선", 1L, 1L);
        final LineEntity lineEntity2 = new LineEntity("1호선", 2L, 2L);

        stationDao.saveStation(EXPRESS_BUS_TERMINAL_STATION);
        stationDao.saveStation(SAPYEONG_STATION);

        final List<LineEntity> lineEntities = lineDao.saveLineEntities(List.of(lineEntity, lineEntity2));
        assertThat(lineEntities.size()).isEqualTo(2);
    }

    @Test
    void findAllStationsOfLine() {




        

    }

}
