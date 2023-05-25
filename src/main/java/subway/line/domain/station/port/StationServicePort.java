package subway.line.domain.station.port;

import org.springframework.stereotype.Component;
import subway.line.domain.station.UnRegisteredStation;
import subway.line.domain.station.application.StationService;
import subway.line.domain.station.application.dto.StationUpdatingInfo;
import subway.line.domain.station.presentation.dto.StationRequest;
import subway.line.domain.station.presentation.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StationServicePort {
    private final StationService stationService;

    public StationServicePort(StationService stationService) {
        this.stationService = stationService;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        final var station = new UnRegisteredStation(stationRequest.getName());
        final var savedStation = stationService.saveStation(station);
        return new StationResponse(savedStation.getId(), savedStation.getName());
    }

    public List<StationResponse> findAllStationResponses() {
        final var stations = stationService.findAllStations();
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponse findById(Long id) {
        final var station = stationService.findById(id);
        return StationResponse.of(station);
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        final var station = stationService.findById(id);
        final var updatingInfo = new StationUpdatingInfo(stationRequest.getName());
        stationService.updateStation(station, updatingInfo);
    }

    public void deleteStationById(Long id) {
        stationService.deleteStationById(id);
    }
}
