package subway.line.application.strategy.sectionsaving;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.application.exception.WrongStrategyMappedException;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
@Order(2)
public class LowestSectionSavingStrategy implements SectionSavingStrategy {
    private final SectionService sectionService;

    public LowestSectionSavingStrategy(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var previousSection = line.findSectionByPreviousStation(previousStation);
        return previousSection.isPresent() && previousSection.get().isNextStationEmpty();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var sectionToUpdate = line.findSectionByPreviousStation(previousStation)
                .orElseThrow(WrongStrategyMappedException::new);

        final var savedSection = sectionService.insert(line.getId(), nextStation, new EmptyStation(), new EmptyDistance());

        final var updatedSection = sectionToUpdate.change()
                .previousStation(previousStation)
                .nextStation(nextStation)
                .distance(distance)
                .done();
        sectionService.update(updatedSection);

        line.addSection(savedSection);
        line.updateSection(updatedSection);
        return savedSection.getId();
    }
}
