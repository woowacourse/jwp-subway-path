package subway.application.v2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.request.CreateStationRequest;
import subway.dao.entity.StationEntity;
import subway.dao.v2.StationDaoV2;
import subway.domain.StationDomain;
import subway.dto.StationResponse;

@Transactional(readOnly = true)
@Service
public class StationServiceV2 {

    private final StationDaoV2 stationDao;

    public StationServiceV2(final StationDaoV2 stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Long saveStation(final CreateStationRequest request) {
        final StationEntity stationEntity = new StationEntity(request.getStationName());

        return stationDao.insert(stationEntity);
    }

    public StationResponse findByStationId(final Long stationId) {
        final StationDomain station = stationDao.findByStationId(stationId)
                .orElseThrow(() -> new IllegalArgumentException("역 식별자값으로 등록된 역이 존재하지 않습니다."));

        return StationResponse.from(station);
    }
}
