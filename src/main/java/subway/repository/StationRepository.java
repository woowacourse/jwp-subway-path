package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

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
        StationEntity stationEntity = stationDao.findByStationId(stationId);
        return new Station(stationEntity.getName());
    }

    public Long findStationIdByStationName(final String stationName) {
        StationEntity stationEntity = stationDao.findByName(stationName);
        return stationEntity.getStationId();
    }

    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(station -> new Station(station.getName()))
                .collect(Collectors.toList());
    }

    public void deleteByStationId(final Long stationId) {
        stationDao.deleteByStationId(stationId);
    }
}
