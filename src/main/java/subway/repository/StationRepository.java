package subway.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.exception.DataConstraintViolationException;
import subway.exception.DuplicatedDataException;
import subway.exception.NotFoundDataException;

import java.util.List;
import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(final Station station) {
        StationEntity stationEntity = StationEntity.from(station);
        validateDuplicate(stationEntity);
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

    public void update(final Station station) {
        StationEntity stationEntity = new StationEntity(station.getId(), station.getName());
        validateExists(stationEntity);
        stationDao.update(stationEntity);
    }

    public void delete(final Station station) {
        try {
            stationDao.deleteById(station.getId());
        } catch (DataIntegrityViolationException exception) {
            throw new DataConstraintViolationException("해당 역을 삭제할 수 없습니다.");
        }
    }

    private void validateExists(final StationEntity stationEntity) {
        Optional<StationEntity> maybeStation = stationDao.findById(stationEntity.getId());
        if (maybeStation.isEmpty()) {
            throw new NotFoundDataException("해당 역 존재하지 않습니다.");
        }
    }

    private void validateDuplicate(final StationEntity stationEntity) {
        List<StationEntity> maybeStation = stationDao.findByName(stationEntity);
        if (!maybeStation.isEmpty()) {
            throw new DuplicatedDataException("이미 존재하는 역이 있습니다.");
        }
    }
}
