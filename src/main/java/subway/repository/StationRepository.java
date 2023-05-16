package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }


    public Station insert(Station station) {
        final StationEntity entity = stationDao.insert(toEntity(station));
        return mapFrom(entity);
    }

    public List<Station> findAll() {
        final List<StationEntity> entities = stationDao.findAll();
        return entities.stream()
                .map(this::mapFrom)
                .collect(toList());
    }

    private Station mapFrom(StationEntity entity) {
        return new Station(entity.getId(), entity.getName());
    }

    public Station findById(Long id) {
        final StationEntity entity = stationDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 역을 찾을 수 없습니다."));
        return new Station(entity.getId(), entity.getName());
    }

    private StationEntity toEntity(Station station) {
        return new StationEntity(station.getId(), station.getName());
    }

    public void update(Station newStation) {
        stationDao.update(toEntity(newStation));
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
