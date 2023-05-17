package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.repository.StationRepository;
import subway.ui.dto.StationRequest;
import subway.ui.dto.StationResponse;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return new StationResponse(station.getId(), station.getName());
    }

    public StationResponse findStationById(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 존재하지 않습니다."));
        return new StationResponse(station.getId(), station.getName());
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
