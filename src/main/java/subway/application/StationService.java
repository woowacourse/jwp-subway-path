package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class StationService {
    private final StationRepository stationRepositoryImpl;

    public StationService(StationRepository stationRepository) {
        this.stationRepositoryImpl = stationRepository;
    }

    @Transactional
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

    @Transactional
    public void updateStation(Long id, StationRequest stationRequest) {
        stationRepositoryImpl.update(new Station(id, stationRequest.getName()));
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepositoryImpl.deleteById(id);
    }

    @Transactional
    public void deleteAll() {
        stationRepositoryImpl.deleteAll();
    }
}