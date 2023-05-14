package subway.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import subway.line.service.LineService;
import subway.station.service.StationService;
import subway.subwayMap.domain.SubwayMap;

@Component
public class SectionEventHandler {

    private final SubwayMap subwayMap;
    private final LineService lineService;
    private final StationService stationService;

    public SectionEventHandler(final SubwayMap subwayMap, final LineService lineService, final StationService stationService) {
        this.subwayMap = subwayMap;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @EventListener
    public void addStation(SaveSectionEvent event) {
        subwayMap.addStation(lineService.findLineById(event.getLineId()),
                stationService.findStationById(event.getUpStation()),
                stationService.findStationById(event.getDownStation()),
                event.isDirection());
    }

    @EventListener
    public void deleteStation(DeleteSectionEvent event) {
        subwayMap.deleteStation(lineService.findLineById(event.getLineId()),
                stationService.findStationById(event.getStationId()));
    }
}
