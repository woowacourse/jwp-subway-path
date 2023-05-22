package subway.line.application.strategy.stationdeleting;

import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.application.exception.WrongStrategyMappedException;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.station.Station;

@Component
public class MiddleStationDeletingStrategy implements StationDeletingStrategy {
    private final SectionService sectionService;

    public MiddleStationDeletingStrategy(SectionService sectionService) {
        this.sectionService = sectionService;
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
                .orElseThrow(WrongStrategyMappedException::new);
        final var previousSection = line.findSectionByNextStation(station)
                .orElseThrow(WrongStrategyMappedException::new)
                .change()
                .nextStation(section.getNextStation())
                .addDistance(section.getDistance())
                .done();

        sectionService.delete(section);
        sectionService.update(previousSection);

        line.removeSection(section);
        line.updateSection(previousSection);
    }
}
