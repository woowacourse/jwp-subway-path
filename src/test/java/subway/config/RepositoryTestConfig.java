package subway.config;

import org.junit.jupiter.api.BeforeEach;
import subway.dao.v2.SectionDaoV2;
import subway.dao.v2.StationDaoV2;

public abstract class RepositoryTestConfig extends DaoTestConfig {

    protected StationDaoV2 stationDaoV2;
    protected SectionDaoV2 sectionDaoV2;

    @BeforeEach
    void daoSetUp() {
        stationDaoV2 = new StationDaoV2(jdbcTemplate);
        sectionDaoV2 = new SectionDaoV2(jdbcTemplate);
    }
}
