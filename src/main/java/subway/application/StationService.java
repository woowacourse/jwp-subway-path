package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.exception.NotFoundException;
import subway.repository.StationRepository;

@Transactional
@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station createStation(StationRequest request) {
        return stationRepository.saveStation(new Station(null, request.getName()));
    }

    @Transactional(readOnly = true)
    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long stationId) {
        return stationRepository.findStationById(stationId)
                .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
    }

    public void updateStation(Long stationId, StationRequest request) {
        validateExistStation(stationId);
        validateDuplicatedName(request.getName());
        stationRepository.updateStation(stationId, request.getName());
    }

    private void validateExistStation(Long stationId) {
        if(stationRepository.findStationById(stationId).isEmpty()) {
            throw new NotFoundException("해당 역이 존재하지 않습니다.");
        }
    }

    private void validateDuplicatedName(String name) {
        if(stationRepository.findStationByName(name).isPresent()) {
            throw new IllegalArgumentException("이미 해당 역의 이름이 존재합니다.");
        }
    }

    public void removeStation(Long stationId) {
        Station station = findStationById(stationId);
        stationRepository.delete(station);
    }
}
