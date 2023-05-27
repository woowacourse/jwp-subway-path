package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.exception.notfound.StationNotFoundException;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long save(final Station station) {
        return stationDao.save(new StationEntity(station.getName()));
    }

    public void deleteById(final Long id) {
        final boolean exist = stationDao.isIdExist(id);
        if (exist) {
            stationDao.deleteById(id);
            return;
        }
        throw new StationNotFoundException();
    }

    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(station -> new Station(station.getStationId(), station.getName()))
                .collect(Collectors.toList());
    }

    public Station findById(final Long id) {
        final boolean exist = stationDao.isIdExist(id);
        if (exist) {
            StationEntity stationEntity = stationDao.findById(id);
            return new Station(stationEntity.getStationId(), stationEntity.getName());
        }
        throw new StationNotFoundException();
    }

    public Station findByName(final String name) {
        final boolean exist = stationDao.isNameExist(name);
        if (exist) {
            final StationEntity stationEntity = stationDao.findByName(name);
            return new Station(stationEntity.getStationId(), stationEntity.getName());
        }
        throw new StationNotFoundException();
    }
}
