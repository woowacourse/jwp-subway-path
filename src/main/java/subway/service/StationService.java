package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.dto.request.StationCreateRequest;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station saveStation(StationCreateRequest createRequest) {
        stationRepository.checkStationIsExist(createRequest.getStationName());
        Station station = new Station(createRequest.getStationName());
        return stationRepository.insert(station);
    }
}
