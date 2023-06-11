package subway.service.station;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;

@Transactional
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long createStation(final StationRequest stationRequest) {
        return stationRepository.save(new Station(stationRequest.getName()));
    }

    public void removeStation(final Long id) {
        stationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StationsResponse findAll() {
        List<StationResponse> stations = stationRepository.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return StationsResponse.from(stations);
    }

    @Transactional(readOnly = true)
    public StationResponse findById(final Long id) {
        Station station = stationRepository.findById(id);
        return StationResponse.from(station);
    }
}
