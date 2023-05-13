package subway.application.v2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateStationRequest;
import subway.dao.entity.StationEntity;
import subway.domain.StationDomain;
import subway.dto.StationResponse;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class StationServiceV2 {

    private final StationRepository stationRepository;

    public StationServiceV2(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Long saveStation(final CreateStationRequest request) {
        final StationEntity stationEntity = new StationEntity(request.getStationName());

        return stationRepository.saveStation(stationEntity);
    }

    public StationResponse findByStationId(final Long stationId) {
        final StationDomain station = stationRepository.findByStationId(stationId);

        return StationResponse.from(station);
    }
}
