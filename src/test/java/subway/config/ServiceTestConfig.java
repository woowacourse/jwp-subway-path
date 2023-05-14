package subway.config;

import org.junit.jupiter.api.BeforeEach;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

public class ServiceTestConfig extends RepositoryTestConfig {

    protected SectionRepository sectionRepository;
    protected StationRepository stationRepository;
    protected LineRepository lineRepository;

    @BeforeEach
    void serviceInit() {
        sectionRepository = new SectionRepository(sectionDao, stationDao);
        stationRepository = new StationRepository(stationDao);
        lineRepository = new LineRepository(lineDao, sectionDao, stationDao);
    }
}
