package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.application.LineRepository;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
@Order(0)
public class InitializingSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public InitializingSectionInsertionStrategy(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        return sectionRepository.countStations(line) == 0;
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var sectionId = sectionRepository.insert(line, previousStation, nextStation, distance).getId();
        sectionRepository.insert(line, nextStation, new EmptyStation(), new EmptyDistance());
        lineRepository.updateHeadStation(line, previousStation);
        return sectionId;
    }
}
