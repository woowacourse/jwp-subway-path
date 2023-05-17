package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationEntity;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        final Station station = stationRepository.save(new StationEntity(stationRequest.getName()));
        return StationResponse.of(station);
    }
}
