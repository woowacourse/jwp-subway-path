package subway.service.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.service.station.domain.Station;
import subway.service.station.dto.StationCreateResponse;
import subway.service.station.dto.StationInsertRequest;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationCreateResponse saveStation(StationInsertRequest stationInsertRequest) {
        Station station = new Station(stationInsertRequest.getName());
        Station savedStation = stationRepository.insert(station);
        return StationCreateResponse.from(savedStation);
    }

    @Transactional(readOnly = true)
    public StationCreateResponse findStationById(Long id) {
        return StationCreateResponse.from(stationRepository.findById(id));
    }
}
