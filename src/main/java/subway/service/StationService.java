package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.subway.Station;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationsResponse;
import subway.repository.StationRepository;

import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public long saveStation(final StationCreateRequest stationCreateRequest) {
        return stationRepository.insertStation(new Station(stationCreateRequest.getName()));
    }

    @Transactional(readOnly = true)
    public StationResponse findStationEntityById(final Long id) {
        Station station = stationRepository.findByStationId(id);
        return StationResponse.from(station);
    }

    @Transactional(readOnly = true)
    public StationsResponse findAllStationResponses() {
        return stationRepository.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), StationsResponse::from));
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
