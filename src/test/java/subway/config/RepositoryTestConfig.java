package subway.config;

import org.junit.jupiter.api.BeforeEach;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;

public abstract class RepositoryTestConfig extends DaoTestConfig {

    protected StationDao stationDao;
    protected SectionDao sectionDao;
    protected LineDao lineDao;

    @BeforeEach
    void daoSetUp() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
    }
}
