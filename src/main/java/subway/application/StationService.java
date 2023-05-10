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

    public void isExistStation(final String station) {
        boolean existStation = stationDao.isExistStationByName(station);

        if (!existStation) {
            throw new IllegalArgumentException("역 없음");
        }
    }

    public Long saveStation(final StationRequest stationRequest) {
        StationEntity stationEntity = stationDao.insert(new Station(stationRequest.getName()));
        return stationEntity.getStationId();
    }

    public StationEntity findStationEntityById(Long id) {
        return stationDao.findById(id);
    }

    public StationEntity findStationByName(final String name) {
        return stationDao.findByName(name);
    }

    public List<StationEntity> findAllStationResponses() {
        return stationDao.findAll();
    }

    public void updateStation(Long id, StationRequest stationRequest) {
//        stationDao.update(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
