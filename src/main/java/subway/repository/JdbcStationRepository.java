package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.entity.StationEntity;
import subway.exception.notfound.StationNotFoundException;

@Repository
public class JdbcStationRepository implements StationRepository {

    private final StationDao stationDao;

    public JdbcStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long save(final Station station) {
        return stationDao.save(new StationEntity(station.getName()));
    }

    @Override
    public void deleteById(final Long id) {
        final boolean exist = stationDao.isIdExist(id);
        if (exist) {
            stationDao.deleteById(id);
            return;
        }
        throw new StationNotFoundException();
    }

    @Override
    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(station -> new Station(station.getStationId(), station.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Station findById(final Long id) {
        final boolean exist = stationDao.isIdExist(id);
        if (exist) {
            StationEntity stationEntity = stationDao.findById(id);
            return new Station(stationEntity.getStationId(), stationEntity.getName());
        }
        throw new StationNotFoundException();
    }

    @Override
    public Station findByName(final String name) {
        final boolean exist = stationDao.isNameExist(name);
        if (exist) {
            final StationEntity stationEntity = stationDao.findByName(name);
            return new Station(stationEntity.getStationId(), stationEntity.getName());
        }
        throw new StationNotFoundException();
    }
}
