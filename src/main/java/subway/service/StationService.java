package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.StationCreateRequest;
import subway.controller.dto.response.StationResponse;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;
import subway.exception.InvalidStationException;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Long createStation(final StationCreateRequest request) {
        final Station station = new Station(request.getName());
        return stationDao.save(new StationEntity(station.getId(), station.getName())).getId();
    }

    public StationResponse findStationById(final Long stationId) {
        final StationEntity stationEntity = stationDao.findById(stationId)
                .orElseThrow(() -> new InvalidStationException("존재하지 않는 역입니다."));
        final Station station = new Station(stationEntity.getId(), stationEntity.getName());
        return StationResponse.from(station);
    }
}
