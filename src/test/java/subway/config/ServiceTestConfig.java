package subway.config;

import org.junit.jupiter.api.BeforeEach;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.StationLineDao;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

public class ServiceTestConfig extends RepositoryTestConfig {

    protected StationDao stationDao;
    protected SectionDao sectionDao;
    protected LineDao lineDao;
    protected StationLineDao stationLineDao;

    protected SectionRepository sectionRepository;
    protected StationRepository stationRepository;
    protected LineRepository lineRepository;

    @BeforeEach
    void serviceInit() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationLineDao = new StationLineDao(jdbcTemplate);

        sectionRepository = new SectionRepository(sectionDaoV2, stationDaoV2);
        stationRepository = new StationRepository(stationDaoV2);
        lineRepository = new LineRepository(lineDaoV2, sectionDaoV2, stationDaoV2);
    }
}
