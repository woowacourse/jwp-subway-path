package subway.dao.station;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.domain.station.StationRepository;


@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    private Station toStation(final StationEntity stationEntity) {
        return new Station(
                stationEntity.getStationId(),
                new StationName(stationEntity.getName())
        );
    }

    @Override
    public Station saveIfNotExist(final Station station) {
        final Optional<Station> findStation = findByName(station.getStationName());

        if (findStation.isPresent()) {
            return findStation.get();
        }

        final StationEntity stationEntity = new StationEntity(station.getStationName());
        return stationDao.insert(stationEntity).toStation();
    }

    private Optional<Station> findByName(final String name) {
        final Set<StationEntity> stations = new HashSet<>(stationDao.findAll());

        return stations.stream()
                .filter(stationEntity -> stationEntity.getName().equals(name))
                .map(this::toStation)
                .findFirst();
    }

    @Override
    public Optional<Station> findById(final Long stationId) {
        final Set<StationEntity> stations = new HashSet<>(stationDao.findAll());

        return stations.stream()
                .filter(stationEntity -> stationEntity.getStationId().equals(stationId))
                .map(this::toStation)
                .findFirst();
    }
}
