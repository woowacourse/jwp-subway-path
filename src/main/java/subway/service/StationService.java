package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.repository.StationRepository;

@Transactional
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long saveStation(final StationRequest stationRequest) {
        return stationRepository.save(new Station(stationRequest.getName()));
    }

    @Transactional(readOnly = true)
    public StationResponse findStationResponseById(final Long id) {
        Station station = stationRepository.findByStationId(id);
        return StationResponse.from(id, station);
    }

    @Transactional(readOnly = true)
    public StationsResponse findAllStationResponses() {
        List<StationResponse> stations = stationRepository.findAll().stream()
                .map(station -> {
                    Long id = stationRepository.findStationIdByStationName(station.getName());
                    return StationResponse.from(id, station);
                })
                .collect(Collectors.toList());

        return StationsResponse.from(stations);
    }

    public void removeStationById(final Long id) {
        stationRepository.deleteByStationId(id);
    }
}
