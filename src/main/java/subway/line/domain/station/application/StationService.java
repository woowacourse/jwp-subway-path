package subway.line.domain.station.application;

import org.springframework.stereotype.Service;
import subway.line.domain.station.Station;
import subway.line.domain.station.dto.StationRequest;
import subway.line.domain.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station saveStation(StationRequest stationRequest) {
        return stationRepository.insert(stationRequest.getName());
    }

    public Station findById(Long id) {
        return stationRepository.findById(id);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationRepository.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}