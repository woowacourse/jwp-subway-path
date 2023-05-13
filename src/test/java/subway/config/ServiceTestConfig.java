package subway.config;

import org.junit.jupiter.api.BeforeEach;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.StationLineDao;
import subway.repository.SectionRepository;

public class ServiceTestConfig extends RepositoryTestConfig {

    protected StationDao stationDao;
    protected SectionDao sectionDao;
    protected LineDao lineDao;
    protected StationLineDao stationLineDao;

    protected SectionRepository sectionRepository;

    @BeforeEach
    void serviceInit() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationLineDao = new StationLineDao(jdbcTemplate);

        sectionRepository = new SectionRepository(sectionDaoV2, stationDaoV2);
    }
}
