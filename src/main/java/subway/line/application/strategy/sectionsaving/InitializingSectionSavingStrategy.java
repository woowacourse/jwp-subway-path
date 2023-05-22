package subway.line.application.strategy.sectionsaving;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.application.LineRepository;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
@Order(0)
public class InitializingSectionSavingStrategy implements SectionSavingStrategy {
    private final SectionService sectionService;
    private final LineRepository lineRepository;

    public InitializingSectionSavingStrategy(SectionService sectionService, LineRepository lineRepository) {
        this.sectionService = sectionService;
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        return line.hasSection();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var previousSection = sectionService.insert(line.getId(), previousStation, nextStation, distance);
        final var nextSection = sectionService.insert(line.getId(), nextStation, new EmptyStation(), new EmptyDistance());
        lineRepository.updateHeadStation(line, previousStation);

        line.addSection(previousSection);
        line.addSection(nextSection);
        line.changeHead(previousStation);
        return previousSection.getId();
    }
}
