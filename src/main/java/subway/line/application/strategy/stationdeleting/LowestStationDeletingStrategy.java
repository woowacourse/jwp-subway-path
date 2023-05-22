package subway.line.application.strategy.stationdeleting;

import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
public class LowestStationDeletingStrategy implements StationDeletingStrategy {
    private final SectionService sectionService;

    public LowestStationDeletingStrategy(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @Override
    public boolean support(Line line, Station station) {
        return line.findSectionByPreviousStation(station)
                .map(Section::isNextStationEmpty)
                .orElse(false);
    }

    @Override
    public void deleteStation(Line line, Station station) {
        final var section = line.findSectionByPreviousStation(station)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        final var newLowestSection = line.findSectionByNextStation(station)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED))
                .change()
                .nextStation(new EmptyStation())
                .distance(new EmptyDistance())
                .done();

        sectionService.delete(section);
        sectionService.update(newLowestSection);

        line.removeSection(section);
        line.updateSection(newLowestSection);
    }
}
