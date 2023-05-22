package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.exception.StationNotFoundException;
import subway.persistence.repository.StationRepository;
import subway.service.dto.StationRequest;
import subway.service.dto.StationResponse;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        final Station station = stationRepository.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(final Long id) {
        final Station station = stationRepository.findById(id).orElseThrow(StationNotFoundException::new);
        return StationResponse.of(station);
    }

    public List<StationResponse> findAllStationResponses() {
        final List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationRepository.update(new Station(id, stationRequest.getName()));
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    public Station findStationByName(final String name) {
        return stationRepository.findByName(name)
                .orElseThrow(StationNotFoundException::new);
    }
}
