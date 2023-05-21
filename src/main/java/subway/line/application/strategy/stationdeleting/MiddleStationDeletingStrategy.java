package subway.line.application.strategy.stationdeleting;

import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.station.Station;

@Component
public class MiddleStationDeletingStrategy implements StationDeletingStrategy {
    private final SectionRepository sectionRepository;

    public MiddleStationDeletingStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station station) {
        return !line.getHead().equals(station)
                && line.findSectionByPreviousStation(station)
                .map(section -> !section.isNextStationEmpty())
                .orElse(false);
    }

    @Override
    public void deleteStation(Line line, Station station) {
        final var section = line.findSectionByPreviousStation(station)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));
        final var previousSection = line.findSectionByNextStation(station)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED))
                .change()
                .nextStation(section.getNextStation())
                .addDistance(section.getDistance())
                .done();

        sectionRepository.delete(section);
        sectionRepository.update(previousSection);

        line.removeSection(section);
        line.updateSection(previousSection);
    }
}
