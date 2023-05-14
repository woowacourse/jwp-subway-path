package subway.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import subway.subwayMap.service.SubwayMapService;

@Component
public class SectionEventHandler {

    private final SubwayMapService stationService;

    public SectionEventHandler(final SubwayMapService stationService) {
        this.stationService = stationService;
    }

    @EventListener
    public void addStation(SaveSectionEvent event) {
        stationService.addStation(event.getLineId(),
                event.getUpStation(),
                event.getDownStation(),
                event.getDirection());
    }

    @EventListener
    public void deleteStation(DeleteSectionEvent event) {
        stationService.deleteStation(event.getLineId(),
                event.getStationId());
    }
}
