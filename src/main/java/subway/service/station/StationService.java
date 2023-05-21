package subway.service.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.persistence.repository.StationRepository;

import java.util.List;

@Transactional
@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        return stationRepository.saveStation(stationRequest);
    }

    public StationResponse findStationById(Long id) {
        return stationRepository.findStationResponseById(id);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAllStationResponses();
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationRepository.updateStation(id, stationRequest);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteStationById(id);
    }
}
