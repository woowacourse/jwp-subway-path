package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationUpdateRequest;
import subway.exception.IllegalStationException;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationCreateRequest stationCreateRequest) {
        Station station = new Station(stationCreateRequest.getStationName());
        if (stationRepository.isDuplicateStation(station)) {
            throw new IllegalStationException("이미 존재하는 역입니다.");
        }
        Long savedId = stationRepository.save(station);
        return StationResponse.from(stationRepository.findById(savedId));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        return stationRepository.findAll()
                .stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public StationResponse updateStation(Long id, StationUpdateRequest stationUpdateRequest) {
        return StationResponse.from(stationRepository.update(new Station(id, stationUpdateRequest.getStationName())));
    }

    public void deleteStationById(Long id) {
        Station station = stationRepository.findById(id);
        stationRepository.delete(station);
    }
}
