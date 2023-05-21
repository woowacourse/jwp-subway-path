package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public void registerStation(final Station station) {
        final Optional<StationEntity> stationEntity = stationDao.findByName(station.getName());
        if (stationEntity.isEmpty()) {
            stationDao.insert(station.getName());
        }
    }

    public List<Station> findStations() {
        final List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(stationEntity -> new Station(stationEntity.getName()))
                .collect(Collectors.toUnmodifiableList());
    }
}
