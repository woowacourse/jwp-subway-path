package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station getStation(Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }

    public void checkStationIsExist(String stationName) {
        stationDao.findByName(stationName)
                .ifPresent(station -> {
                    throw new IllegalArgumentException("이미 존재하는 지하철역입니다.");
                });
    }

    public Station insert(Station station) {
        return stationDao.insert(station);
    }
}
