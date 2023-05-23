package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.StationRequest;
import subway.domain.line.Station;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Station saveStation(StationRequest stationRequest) {
        return stationRepository.save(new Station(null, stationRequest.getName()));
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id);
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    public Station findByName(String name) {
        return stationRepository.findByName(name);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
