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

    public Station findByStationId(final Long stationId) {
        final boolean exist = stationDao.isStationIdExist(stationId);
        if (exist) {
            StationEntity stationEntity = stationDao.findByStationId(stationId);
            return new Station(stationEntity.getName());
        }
        throw new StationNotFoundException();
    }

    public Long findStationIdByStationName(final String stationName) {
        final boolean exist = stationDao.isStationNameExist(stationName);
        if (exist) {
            StationEntity stationEntity = stationDao.findByName(stationName);
            return stationEntity.getStationId();
        }
        throw new StationNotFoundException();
    }

    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(station -> new Station(station.getName()))
                .collect(Collectors.toList());
    }

    public void deleteByStationId(final Long stationId) {
        final boolean exist = stationDao.isStationIdExist(stationId);
        if (exist) {
            stationDao.deleteByStationId(stationId);
            return;
        }
        throw new StationNotFoundException();
    }
}
