package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long save(final StationRequest stationRequest) {
        final Station station = new Station(stationRequest.getName());
        return stationRepository.insert(station);
    }

    public StationResponse findById(final Long id) {
        final Station station = stationRepository.findById(id);
        return StationResponse.of(station);
    }

    public List<StationResponse> findAll() {
        return stationRepository.findAll().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void update(final Long id, final StationRequest stationRequest) {
        final Station station = new Station(stationRequest.getName());
        stationRepository.update(id, station);
    }

    public void deleteById(final Long id) {
        stationRepository.deleteById(id);
    }
}
