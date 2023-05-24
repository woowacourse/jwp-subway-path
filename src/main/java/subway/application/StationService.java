package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.station.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.AlreadyExistStationException;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationRepository.existByName(stationRequest.getName())) {
            throw new AlreadyExistStationException();
        }
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(stationRepository.findById(id));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        Station station = stationRepository.findById(id);
        Station updateReadyStation = new Station(station.getId(), stationRequest.getName());
        stationRepository.save(updateReadyStation);
    }

    public void deleteStationById(Long id) {
        if (stationRepository.registeredLineById(id)) {
            throw new IllegalArgumentException("노선에 등록된 상태에서는 역을 제거할 수 없습니다.");
        }
        stationRepository.deleteById(id);
    }
}
