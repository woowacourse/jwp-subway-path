package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.dto.StationAddRequest;
import subway.dto.StationAddResponse;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationAddResponse createStation(StationAddRequest request) {
        Station station = new Station(request.getStationName());
        Station savedStation = stationRepository.save(station);
        return StationAddResponse.from(savedStation);
    }
}
