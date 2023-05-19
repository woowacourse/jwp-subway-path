package subway.station.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.section.service.SectionService;
import subway.station.presentation.dto.response.StationResponse;
import subway.station.service.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Component
public class StationFacade {

    private final StationService stationService;
    private final SectionService sectionService;

    public StationFacade(final StationService stationService, final SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    @Transactional
    public Long createStation(final String name) {
        return stationService.insert(name);
    }

    public List<StationResponse> getAll() {
        return stationService.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateById(final Long stationId, final String name) {
        stationService.updateById(stationId, name);
    }

    @Transactional
    public void deleteById(final Long lineId, final Long stationId) {
        stationService.ifFinalStationDelete(lineId, stationId);
        sectionService.ifMiddleStationDelete(lineId, stationId);
    }

}
