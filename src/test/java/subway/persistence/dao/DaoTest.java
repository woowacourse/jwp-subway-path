package subway.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test.sql")
@JdbcTest
public class DaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected LineDao lineDao;
    protected StationDao stationDao;
    protected PathDao pathDao;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate);
        this.stationDao = new StationDao(jdbcTemplate);
        this.pathDao = new PathDao(jdbcTemplate);
    }
}
