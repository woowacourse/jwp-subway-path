package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.station.StationDao;
import subway.domain.Station;
import subway.domain.entity.StationEntity;
import subway.dto.station.StationRequest;

import java.util.List;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long saveStation(final StationRequest stationRequest) {
        StationEntity stationEntity = stationDao.insert(new Station(stationRequest.getName()));
        return stationEntity.getStationId();
    }

    public StationEntity findStationEntityById(final Long id) {
        return stationDao.findById(id);
    }

    public StationEntity findStationByName(final String name) {
        return stationDao.findByName(name);
    }

    public List<StationEntity> findAllStationResponses() {
        return stationDao.findAll();
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
