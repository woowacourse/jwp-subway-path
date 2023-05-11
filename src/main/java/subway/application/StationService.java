package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.ui.dto.StationCreateRequest;
import subway.ui.dto.StationResponse;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void createStation(final StationCreateRequest stationCreateRequest) {
        final Station station = new Station(stationCreateRequest.getName());

        stationRepository.createStation(station);
    }

    public List<StationResponse> findAll() {
        return StationResponse.of(stationRepository.findAll());
    }

    public Station findById(final Long stationIdRequest) {
        return stationRepository.findById(stationIdRequest);
    }

    public void deleteById(final Long stationIdRequest) {
        stationRepository.deleteById(stationIdRequest);
    }
}
