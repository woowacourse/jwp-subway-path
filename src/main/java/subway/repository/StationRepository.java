package subway.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.exception.DataConstraintViolationException;
import subway.exception.NotFoundDataException;

import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(final Station station) {
        StationEntity stationEntity = StationEntity.from(station);
        Long id = stationDao.save(stationEntity);
        return new Station(
                id,
                station.getName()
        );
    }

    public Station findById(final Long id) {
        Optional<StationEntity> maybeStationEntity = stationDao.findById(id);
        if (maybeStationEntity.isEmpty()) {
            throw new NotFoundDataException("해당 역은 존재하지 않습니다");
        }
        return maybeStationEntity.get().convertToStation();
    }

    public void update(final Station before, final Station after) {
        StationEntity stationEntity = new StationEntity(before.getId(), after.getName());
        stationDao.update(stationEntity);
    }

    public void delete(final Station station) {
        try {
            stationDao.deleteById(station.getId());
        } catch(DataIntegrityViolationException exception) {
            throw new DataConstraintViolationException("해당 역을 삭제할 수 없습니다.");
        }
    }
}
