package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station saveStation(Station station) {
        Long stationId = stationDao.insert(StationEntity.of(station));
        return new Station(stationId, station.getName());
    }

    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(entity -> new Station(entity.getId(), entity.getName()))
                .collect(Collectors.toList());
    }

    public Optional<Station> findStationById(Long stationId) {
        Optional<StationEntity> entity = stationDao.findById(stationId);
        if(entity.isPresent()) {
            StationEntity station = entity.get();
            return Optional.of(new Station(station.getId(), station.getName()));
        }
        return Optional.empty();
    }

    public Optional<Station> findStationByName(String stationName) {
        Optional<StationEntity> entity = stationDao.findByName(stationName);
        if(entity.isPresent()) {
            StationEntity station = entity.get();
            return Optional.of(new Station(station.getId(), station.getName()));
        }
        return Optional.empty();
    }

    public void updateStation(Long stationId, String name) {
        stationDao.update(new StationEntity(stationId, name));
    }

    public void delete(Station station) {
        stationDao.deleteById(station.getId());
    }

}
