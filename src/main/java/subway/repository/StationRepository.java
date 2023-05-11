package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.station.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long insertStation(final Station station) {
        return stationDao.insert(station);
    }

    public Station findByStationId(final Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId);
        return new Station(stationEntity.getName());
    }

    public Long findStationIdByStationName(final String stationName) {
        StationEntity stationEntity = stationDao.findByName(stationName);
        return stationEntity.getStationId();
    }

    public Station findByStationName(final String stationName) {
        StationEntity stationEntity = stationDao.findByName(stationName);
        return new Station(stationEntity.getName());
    }

    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(station -> new Station(station.getName()))
                .collect(Collectors.toList());
    }

    public void deleteById(final Long stationId) {
        stationDao.deleteById(stationId);
    }
}
