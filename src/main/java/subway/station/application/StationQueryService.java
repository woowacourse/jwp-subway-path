package subway.station.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.exception.StationNotFoundException;
import subway.station.domain.StationRepository;
import subway.station.dto.response.StationInfoResponse;

@Service
@Transactional
public class StationQueryService {

    private final StationRepository stationRepository;

    public StationQueryService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationInfoResponse> findAll() {
        return stationRepository.findAll()
                .stream()
                .map(StationInfoResponse::from)
                .collect(Collectors.toList());
    }

    public StationInfoResponse findStationInfoById(long id) {
        return stationRepository.findById(id)
                .map(StationInfoResponse::from)
                .orElseThrow(StationNotFoundException::new);
    }
}
