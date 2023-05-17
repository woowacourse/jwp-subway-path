package subway.repository;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.repository.dao.StationDao;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(Station station) {
        StationEntity stationEntity = new StationEntity(station.getName());
        StationEntity savedEntity = stationDao.insert(stationEntity);
        return toStation(savedEntity);
    }

    private Station toStation(StationEntity savedEntity) {
        return new Station(savedEntity.getId(), savedEntity.getName());
    }

    public Station findByName(String name) {
        StationEntity stationEntity = stationDao.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다"));
        return toStation(stationEntity);
    }
}
