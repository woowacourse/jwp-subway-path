package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse createStation(final StationCreateRequest request) {
        final Station station = stationRepository.save(new Station(request.getName()));
        return StationResponse.from(station);
    }
}

