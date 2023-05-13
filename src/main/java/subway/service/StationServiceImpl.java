package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Service
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;

    public StationServiceImpl(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public StationResponse createStation(final StationCreateRequest request) {
        final Station station = stationRepository.save(new Station(request.getName()));
        return StationResponse.from(station);
    }
}
