package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long createStation(final Station station) {
        final StationEntity stationEntity = new StationEntity(station.getName());

        return stationDao.createStation(stationEntity);
    }

    @Override
    public Optional<Station> findByName(final Station station) {
        final Set<StationEntity> stations = new HashSet<>(stationDao.findAll());

        return stations.stream()
                .filter(stationEntity -> stationEntity.getName().equals(station.getName()))
                .map(stationEntity -> new Station(stationEntity.getName()))
                .findFirst();
    }
}
