package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.repository.StationRepository;
import subway.ui.dto.request.StationRequest;
import subway.ui.dto.response.StationResponse;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(final StationRequest request) {
        Station station = stationRepository.save(new Station(request.getName()));
        return StationResponse.from(station);
    }

    public StationResponse findStationById(final Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 존재하지 않습니다."));
        return StationResponse.from(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
