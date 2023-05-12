package subway.station.adapter.output.persistence;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;
import subway.station.domain.Station;
import subway.station.domain.StationName;
import subway.station.application.port.output.StationRepository;


@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Optional<Station> findByName(final String name) {
        final Set<StationEntity> stations = new HashSet<>(stationDao.findAll());

        return stations.stream()
                .filter(stationEntity -> stationEntity.getName().equals(name))
                .map(this::toStation)
                .findFirst();
    }

    private Station toStation(final StationEntity stationEntity) {
        return new Station(new StationName(stationEntity.getName()));
    }

    @Override
    public Long save(final Station station) {
        final StationEntity stationEntity = new StationEntity(station.getStationName());

        return stationDao.insert(stationEntity).getStationId();
    }
}
