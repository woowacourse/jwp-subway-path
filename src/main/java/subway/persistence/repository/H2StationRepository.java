package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.StationEntity;
import subway.service.station.StationRepository;
import subway.service.station.domain.Station;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class H2StationRepository implements StationRepository {

    private final StationDao stationDao;

    public H2StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station insert(Station station) {
        StationEntity stationEntity = new StationEntity(station.getName());
        StationEntity savedStationEntity = stationDao.insert(stationEntity);
        return new Station(savedStationEntity.getId(), savedStationEntity.getName());
    }

    @Override
    public List<Station> findStationsById(Set<Long> ids) {
        List<StationEntity> stationsById = stationDao.findStationsById(ids);
        return stationsById.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Station findById(Long id) {
        StationEntity stationEntity = stationDao.findById(id);
        return toDomain(stationEntity);
    }

    @Override
    public void deleteById(long stationId) {
        stationDao.deleteById(stationId);
    }

    private Station toDomain(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
