package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

@Component
@Order(3)
public class DownDirectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionRepository sectionRepository;

    public DownDirectionInsertionStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var previousSection = sectionRepository.findByPreviousStation(previousStation, line);
        return previousSection.isPresent() && !previousSection.get().isNextStationEmpty();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var sectionToUpdate = sectionRepository.findByPreviousStation(previousStation, line)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var section = sectionRepository.insert(line, nextStation, sectionToUpdate.getNextStation(), sectionToUpdate.getDistance().subtract(distance));

        sectionRepository.update(sectionToUpdate.change()
                .nextStation(nextStation)
                .distance(distance)
                .done());

        return section.getId();
    }
}
