package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.station.Station;
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
        final Station station = new Station(stationRequest.getName());
        validateDuplication(station);
        return StationResponse.of(stationRepository.save(station));
    }

    private void validateDuplication(final Station station) {
        if (stationRepository.contains(station)) {
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
    }
}
