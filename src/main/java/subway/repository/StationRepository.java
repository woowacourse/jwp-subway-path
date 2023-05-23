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

    public Station registerStation(final Station station) {
        final Optional<StationEntity> foundedStationEntity = stationDao.findByName(station.getName());
        if (foundedStationEntity.isPresent()) {
            return new Station(foundedStationEntity.get().getName());
        }
        final StationEntity insertedStationEntity = stationDao.insert(station.getName());
        return new Station(insertedStationEntity.getName());
    }

    public List<Station> findStations() {
        final List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(stationEntity -> new Station(stationEntity.getName()))
                .collect(Collectors.toUnmodifiableList());
    }
}
