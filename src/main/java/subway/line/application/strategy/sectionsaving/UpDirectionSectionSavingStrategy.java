package subway.line.application.strategy.sectionsaving;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.application.exception.WrongStrategyMappedException;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

@Component
@Order(1)
public class UpDirectionSectionSavingStrategy implements SectionSavingStrategy {
    private final SectionService sectionService;

    public UpDirectionSectionSavingStrategy(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        return line.findSectionByNextStation(nextStation).isPresent();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var savedSection = sectionService.insert(line.getId(), previousStation, nextStation, distance);

        final var sectionToUpdate = line.findSectionByNextStation(nextStation)
                .orElseThrow(WrongStrategyMappedException::new);

        final var updatedSection = sectionToUpdate.change()
                .nextStation(previousStation)
                .subtractDistance(distance)
                .done();

        sectionService.update(updatedSection);

        line.addSection(savedSection);
        line.updateSection(updatedSection);

        return savedSection.getId();
    }
}
