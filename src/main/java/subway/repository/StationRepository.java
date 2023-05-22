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

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(final Station station) {
        StationEntity stationEntity = stationDao.save(new StationEntity(station.getId(), station.getName()));
        return new Station(stationEntity.getId(), station.getName());
    }

    public List<Station> findAll() {
        return stationDao.findAll()
                .stream()
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName())).collect(
                        Collectors.toList());
    }

    public Optional<Station> findById(final Long id) {
        return stationDao.findById(id)
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()));
    }

    public Optional<Station> findByName(final String name) {
        return stationDao.findByName(name)
                .map(stationEntity -> new Station(stationEntity.getId(), stationEntity.getName()));
    }

    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }
}
