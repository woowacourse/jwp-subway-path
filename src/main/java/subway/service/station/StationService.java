package subway.service.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.station.dto.StationCreateRequest;
import subway.service.station.domain.Station;
import subway.service.station.dto.StationCreateResponse;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationCreateResponse saveStation(StationCreateRequest stationCreateRequest) {
        Station station = new Station(stationCreateRequest.getName());
        Station savedStation = stationRepository.insert(station);
        return StationCreateResponse.of(savedStation);
    }

    @Transactional(readOnly = true)
    public StationCreateResponse findStationById(Long id) {
        return StationCreateResponse.of(stationRepository.findById(id));
    }
}
