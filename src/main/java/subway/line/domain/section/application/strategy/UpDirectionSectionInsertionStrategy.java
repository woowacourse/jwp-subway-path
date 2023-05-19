package subway.line.domain.section.application.strategy;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

@Component
@Order(1)
public class UpDirectionSectionInsertionStrategy implements SectionInsertionStrategy {
    private final SectionRepository sectionRepository;

    public UpDirectionSectionInsertionStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station previousStation, Station nextStation, Distance distance) {
        return sectionRepository.findByNextStation(nextStation, line).isPresent();
    }

    @Override
    public long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        final var savedSection = sectionRepository.insert(line, previousStation, nextStation, distance);

        final var stationToUpdate = sectionRepository.findByNextStation(nextStation, line)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));
        sectionRepository.update(stationToUpdate.change()
                .line(line)
                .nextStation(previousStation)
                .subtractDistance(distance)
                .done());

        return savedSection.getId();
    }
}
