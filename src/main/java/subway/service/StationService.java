package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long save(final StationCreateRequest stationCreateRequest) {
        Station station = Station.createWithoutId(stationCreateRequest.getName());

        return stationRepository.save(station);
    }

    public StationResponse findById(final Long id) {
        Station station = stationRepository.findById(id);

        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
