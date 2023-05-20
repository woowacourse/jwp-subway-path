package subway.line.application.strategy.sectionsaving;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

@Component
@Order(3)
public class DownDirectionSavingStrategy implements SectionSavingStrategy {
    private final SectionRepository sectionRepository;

    public DownDirectionSavingStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var previousSection = line.findByPreviousStation(previousStation);
        return previousSection.isPresent() && !previousSection.get().isNextStationEmpty();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var sectionToUpdate = line.findByPreviousStation(previousStation)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var section = sectionRepository.insert(line.getId(), nextStation, sectionToUpdate.getNextStation(), sectionToUpdate.getDistance().subtract(distance));

        final var updatedSection = sectionRepository.update(sectionToUpdate.change()
                .nextStation(nextStation)
                .distance(distance)
                .done());

        line.addSection(section);
        line.updateSection(updatedSection);
        return section.getId();
    }
}
