package subway.config;

import org.junit.jupiter.api.BeforeEach;
import subway.dao.v2.SectionDaoV2;
import subway.dao.v2.StationDaoV2;

public abstract class RepositoryTestConfig extends DaoTestConfig {

    protected StationDaoV2 stationDao;
    protected SectionDaoV2 sectionDao;

    @BeforeEach
    void daoSetUp() {
        stationDao = new StationDaoV2(jdbcTemplate);
        sectionDao = new SectionDaoV2(jdbcTemplate);
    }
}
