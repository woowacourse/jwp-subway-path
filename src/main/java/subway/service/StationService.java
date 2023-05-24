package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.line.Station;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station saveStation(String stationName) {
        stationRepository.checkStationIsExist(stationName);
        Station station = new Station(stationName);
        return stationRepository.insert(station);
    }
}
