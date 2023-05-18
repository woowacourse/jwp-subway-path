package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationEntity;
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
        final StationEntity stationEntity = new StationEntity(stationRequest.getName());
        validateDuplication(stationEntity);
        return StationResponse.of(stationRepository.save(stationEntity));
    }

    private void validateDuplication(final StationEntity stationEntity) {
        if (stationRepository.contains(stationEntity)) {
            throw new IllegalArgumentException("이미 존재하는 역입니다.");
        }
    }
}
