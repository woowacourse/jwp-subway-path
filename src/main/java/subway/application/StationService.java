package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Service
public class StationService {
    private final StationRepository stationRepositoryImpl;

    public StationService(StationRepository stationRepository) {
        this.stationRepositoryImpl = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepositoryImpl.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(stationRepositoryImpl.findById(id));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationRepositoryImpl.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationRepositoryImpl.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationRepositoryImpl.deleteById(id);
    }
}