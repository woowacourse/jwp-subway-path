package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveStation(final StationRequest request) {
        final Station station = new Station(new StationName(request.getName()));
        return stationRepository.insert(station);
    }

    public StationResponse findStationById(Long id) {
        final Station station = stationRepository.findById(id);
        return new StationResponse(station.getId(), station.getName().name());
    }

    public List<StationResponse> findAllStation() {
        final List<Station> stations = stationRepository.findAll();
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName().name()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void update(final Long id, final StationRequest stationRequest) {
        final Station updateStation = new Station(id, new StationName(stationRequest.getName()));
        stationRepository.update(updateStation);
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
