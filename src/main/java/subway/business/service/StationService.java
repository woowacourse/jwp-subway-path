package subway.business.service;

import org.springframework.stereotype.Service;
import subway.business.domain.Station;
import subway.business.domain.StationRepository;
import subway.business.service.dto.StationRequest;
import subway.business.service.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }


    public long saveStation(StationRequest stationRequest) {
        return stationRepository.create(new Station(stationRequest.getName()));
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
