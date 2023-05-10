package subway.service;

import org.springframework.stereotype.Service;
import subway.controller.dto.StationCreateRequest;
import subway.controller.dto.StationResponse;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long createStation(final StationCreateRequest request) {
        final Station station = new Station(request.getName());
        return stationDao.save(StationEntity.from(station)).getId();
    }

    public StationResponse findStationById(final Long stationId) {
        return null;
    }
}
