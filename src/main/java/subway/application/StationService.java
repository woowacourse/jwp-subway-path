package subway.application;

import org.springframework.stereotype.Service;
import subway.application.request.StationRequest;
import subway.application.response.StationResponse;
import subway.domain.station.Station;
import subway.persistence.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public long saveStation(final StationRequest stationRequest) {
        return stationRepository.saveSection(stationRequest);
    }

    public StationResponse findStationResponseById(final Long id) {
        final Station station = stationRepository.findStationById(id);
        return new StationResponse(station.getId(), station.getName().getValue());
    }

    public List<StationResponse> findAllStationResponses() {
        return stationRepository.findAllStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName().getValue()))
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationRepository.updateStation(id, stationRequest);
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteStationById(id);
    }
}
