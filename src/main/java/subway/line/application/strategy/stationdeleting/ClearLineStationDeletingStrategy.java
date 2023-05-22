package subway.line.application.strategy.stationdeleting;

import org.springframework.stereotype.Component;
import subway.line.Line;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

@Component
public class ClearLineStationDeletingStrategy implements StationDeletingStrategy {
    private final SectionService sectionService;

    public ClearLineStationDeletingStrategy(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @Override
    public boolean support(Line line, Station station) {
        return line.getStationsSize() == Line.MIN_STATIONS_SIZE;
    }

    @Override
    public void deleteStation(Line line, Station station) {
        sectionService.clearStations(line.getId());

        line.clearSection();
        line.changeHead(new EmptyStation());
    }
}
