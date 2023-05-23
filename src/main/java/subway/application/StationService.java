package subway.application;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationAddRequest;
import subway.repository.StationRepository;

@Transactional
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station createStation(StationAddRequest request) {
        Station station = new Station(request.getStationName());
        return stationRepository.save(station);
    }

    public void deleteStation(Long stationId) {
        stationRepository.deleteById(stationId);
    }

    @Transactional(readOnly = true)
    public Station findByName(String stationName) {
        return stationRepository.findByName(stationName)
                .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다"));
    }
}
