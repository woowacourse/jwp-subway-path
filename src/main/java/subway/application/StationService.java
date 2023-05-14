package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.H2StationRepository;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {

    private final H2StationRepository stationRepository;

    public StationService(H2StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(stationRepository.findById(id));
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
