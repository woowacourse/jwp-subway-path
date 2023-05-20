package subway.line.application.strategy.sectionsaving;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

@Component
@Order(1)
public class UpDirectionSectionSavingStrategy implements SectionSavingStrategy {
    private final SectionRepository sectionRepository;

    public UpDirectionSectionSavingStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        return line.findByNextStation(nextStation).isPresent();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var savedSection = sectionRepository.insert(line.getId(), previousStation, nextStation, distance);

        final var sectionToUpdate = line.findByNextStation(nextStation)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var updatedSection = sectionToUpdate.change()
                .nextStation(previousStation)
                .subtractDistance(distance)
                .done();

        sectionRepository.update(updatedSection);

        line.addSection(savedSection);
        line.updateSection(updatedSection);

        return savedSection.getId();
    }
}
