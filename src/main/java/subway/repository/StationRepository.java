package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findByName(String stationName) {
        StationEntity stationEntity = stationDao.findByName(stationName).orElseThrow(RuntimeException::new);

        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public List<Station> findAll() {
        return stationDao.findAll().stream().map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName())).collect(Collectors.toList());
    }

    public Station findById(Long id) {
        StationEntity stationEntity = stationDao.findById(id).orElseThrow(RuntimeException::new);

        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Station save(Station station) {
        if (stationDao.existsById(station.getId())) {
            StationEntity updated = stationDao.update(new StationEntity(station.getId(), station.getName()));
            return new Station(updated.getId(), updated.getName());
        }
        StationEntity inserted = stationDao.insert(new StationEntity(station.getId(), station.getName()));
        return new Station(inserted.getId(), inserted.getName());
    }
}
