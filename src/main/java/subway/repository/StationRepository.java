package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineStationDao;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;
import subway.exception.NoSuchStationException;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private final StationDao stationDao;
    private final LineStationDao lineStationDao;

    public StationRepository(StationDao stationDao, LineStationDao lineStationDao) {
        this.stationDao = stationDao;
        this.lineStationDao = lineStationDao;
    }

    public List<Station> findAll() {
        return stationDao.findAll().stream().map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName())).collect(Collectors.toList());
    }

    public Station findById(Long id) {
        StationEntity stationEntity = stationDao.findById(id).orElseThrow(NoSuchStationException::new);

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

    public boolean registeredLineById(Long id) {
        return lineStationDao.findByStationId(id).size() >= 1;
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }

    public boolean existByName(String name) {
        return stationDao.existsByName(name);
    }
}
