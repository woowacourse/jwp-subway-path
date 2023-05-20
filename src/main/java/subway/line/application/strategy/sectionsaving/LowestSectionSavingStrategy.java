package subway.line.application.strategy.sectionsaving;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
@Order(2)
public class LowestSectionSavingStrategy implements SectionSavingStrategy {
    private final SectionRepository sectionRepository;

    public LowestSectionSavingStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var previousSection = line.findByPreviousStation(previousStation);
        return previousSection.isPresent() && previousSection.get().isNextStationEmpty();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var sectionToUpdate = line.findByPreviousStation(previousStation)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var savedSection = sectionRepository.insert(line.getId(), nextStation, new EmptyStation(), new EmptyDistance());

        final var updatedSection = sectionToUpdate.change()
                .previousStation(previousStation)
                .nextStation(nextStation)
                .distance(distance)
                .done();
        sectionRepository.update(updatedSection);

        line.addSection(savedSection);
        line.updateSection(updatedSection);
        return savedSection.getId();
    }
}
