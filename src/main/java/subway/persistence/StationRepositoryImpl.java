package subway.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station save(final Station station) {
        final StationEntity stationEntity = StationEntity.from(station);
        final StationEntity savedStationEntity = stationDao.insert(stationEntity);
        return new Station(savedStationEntity.getId(), station.getName());
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll()
            .stream()
            .map(this::makeStation)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Station> findById(final Long id) {
        final StationEntity stationEntity = stationDao.findById(id);
        if (stationEntity == null) {
            return Optional.empty();
        }
        final Station station = makeStation(stationEntity);
        return Optional.of(station);
    }

    @Override
    public void update(final Station station) {
        final StationEntity stationEntity = StationEntity.from(station);
        stationDao.update(stationEntity);
    }

    @Override
    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }

    @Override
    public Optional<Station> findByName(final String stationName) {
        final StationEntity stationEntity = stationDao.findByName(stationName);
        if (stationEntity == null) {
            return Optional.empty();
        }
        final Station station = makeStation(stationEntity);
        return Optional.of(station);
    }

    private Station makeStation(final StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
