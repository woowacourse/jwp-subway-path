package subway.domain.station.service;

import org.springframework.stereotype.Service;
import subway.domain.station.dao.StationDao;
import subway.domain.station.entity.StationEntity;
import subway.domain.station.dto.StationRequest;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationEntity saveStation(final StationRequest stationRequest) {
        Optional<StationEntity> findStation = stationDao.findByName(stationRequest.getName());

        if (findStation.isPresent()) {
            throw new IllegalArgumentException("역 이름이 이미 존재합니다. 유일한 역 이름을 사용해주세요.");
        }

        return stationDao.insert(new StationEntity(stationRequest.getName()));
    }

    public StationEntity findStationById(final Long id) {
        return stationDao.findById(id);
    }

    public List<StationEntity> findAllStation() {
        return stationDao.findAll();
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new StationEntity(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
