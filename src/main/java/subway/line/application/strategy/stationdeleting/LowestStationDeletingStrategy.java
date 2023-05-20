package subway.line.application.strategy.stationdeleting;

import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
public class LowestStationDeletingStrategy implements StationDeletingStrategy {
    private final SectionRepository sectionRepository;

    public LowestStationDeletingStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station station) {
        return line.findByPreviousStation(station)
                .map(Section::isNextStationEmpty)
                .orElse(false);
    }

    @Override
    public void deleteStation(Line line, Station station) {
        final var section = line.findByPreviousStation(station)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var newLowestSection = line.findByNextStation(station)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED))
                .change()
                .nextStation(new EmptyStation())
                .distance(new EmptyDistance())
                .done();

        sectionRepository.delete(section);
        sectionRepository.update(newLowestSection);

        line.removeSection(section);
        line.updateSection(newLowestSection);
    }
}
