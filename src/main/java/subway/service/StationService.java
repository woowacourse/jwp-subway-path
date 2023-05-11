package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.station.StationRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveStation(final StationRequest stationRequest) {
        return stationRepository.insertStation(new Station(stationRequest.getName()));
    }

    @Transactional(readOnly = true)
    public StationResponse findStationEntityById(final Long id) {
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

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
