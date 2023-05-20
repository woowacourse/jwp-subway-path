package subway.line.application.strategy.sectionsaving;

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
public class InitializingSectionSavingStrategy implements SectionSavingStrategy {
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public InitializingSectionSavingStrategy(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        return line.hasSection();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var previousSection = sectionRepository.insert(line.getId(), previousStation, nextStation, distance);
        final var nextSection = sectionRepository.insert(line.getId(), nextStation, new EmptyStation(), new EmptyDistance());
        lineRepository.updateHeadStation(line, previousStation);

        line.addSection(previousSection);
        line.addSection(nextSection);
        line.changeHead(previousStation);
        return previousSection.getId();
    }
}
