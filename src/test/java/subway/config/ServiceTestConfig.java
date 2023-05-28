package subway.config;

import org.junit.jupiter.api.BeforeEach;
import subway.repository.FareRepository;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

public abstract class ServiceTestConfig extends RepositoryTestConfig {

    protected SectionRepository sectionRepository;
    protected StationRepository stationRepository;
    protected LineRepository lineRepository;
    protected FareRepository fareRepository;

    @BeforeEach
    void serviceInit() {
        sectionRepository = new SectionRepository(sectionDao, stationDao);
        stationRepository = new StationRepository(stationDao);
        lineRepository = new LineRepository(lineDao, sectionDao, stationDao);
        fareRepository = new FareRepository(lineExpenseDao);
    }
}
