package subway.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.persistence.repository.SubwayRepository;


@Sql({"/scheme.sql", "/data.sql"})
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@JdbcTest
public abstract class DaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected LineDao lineDao;
    protected StationDao stationDao;
    protected PathDao pathDao;
    protected SubwayRepository subwayRepository;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
        this.stationDao = new StationDao(jdbcTemplate);
        this.pathDao = new PathDao(jdbcTemplate);
        this.subwayRepository = new SubwayRepository(lineDao, stationDao, pathDao);
    }
}
