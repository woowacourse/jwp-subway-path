package subway.line.application.strategy.stationdeleting;

import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
public class ClearLineStationDeletingStrategy implements StationDeletingStrategy {
    private final SectionRepository sectionRepository;

    public ClearLineStationDeletingStrategy(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public boolean support(Line line, Station station) {
        return line.getStationsSize() == Line.MIN_STATIONS_SIZE;
    }

    @Override
    public void deleteStation(Line line, Station station) {
        sectionRepository.clearStations(line);

        line.clearSection();
        line.changeHead(new EmptyStation());
    }
}
