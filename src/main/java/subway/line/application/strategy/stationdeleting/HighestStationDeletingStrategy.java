package subway.line.application.strategy.stationdeleting;

import org.springframework.stereotype.Component;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.application.LineRepository;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.station.Station;

@Component
public class HighestStationDeletingStrategy implements StationDeletingStrategy {
    private SectionRepository sectionRepository;
    private LineRepository lineRepository;

    public HighestStationDeletingStrategy(SectionRepository sectionRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Override
    public boolean support(Line line, Station station) {
        return Line.MIN_STATIONS_SIZE < line.getStationsSize()
                && line.getHead().equals(station);
    }

    @Override
    public void deleteStation(Line line, Station station) {
        final var section = line.findByPreviousStation(station)
                .orElseThrow(() -> new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED));

        lineRepository.updateHeadStation(line, section.getNextStation());
        sectionRepository.delete(section);

        line.changeHead(section.getNextStation());
        line.removeSection(section);
    }
}
