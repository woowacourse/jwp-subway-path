package subway.application.station;

import org.springframework.stereotype.Service;
import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.ui.dto.request.StationCreateRequest;

import java.util.Optional;

@Service
public class CreateStationService {
    private final StationRepository stationRepository;

    public CreateStationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Long createStation(final StationCreateRequest stationCreateRequest) {
        final Station station = new Station(stationCreateRequest.getName());

        final Optional<Station> findByNameStation = stationRepository.findByName(station);

        if (findByNameStation.isEmpty()) {
            return stationRepository.createStation(station);
        }
        throw new IllegalArgumentException("이미 등록된 역입니다.");
    }
}
