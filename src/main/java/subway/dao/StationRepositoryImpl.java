package subway.dao;

import static subway.exception.ErrorCode.DB_DELETE_ERROR;
import static subway.exception.ErrorCode.DB_UPDATE_ERROR;
import static subway.exception.ErrorCode.STATION_NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.domain.station.dto.StationRes;
import subway.exception.DBException;
import subway.exception.NotFoundException;

@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryImpl(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long insert(final Station station) {
        final StationEntity stationEntity = new StationEntity(station.name().name());
        return stationDao.insert(stationEntity);
    }

    @Override
    public List<StationRes> findAll() {
        final List<StationEntity> stations = stationDao.findAll();
        return stations.stream()
            .map(station -> new StationRes(station.getId(), station.getName()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Station findById(final Long id) {
        final StationEntity stationEntity = stationDao.findById(id)
            .orElseThrow(() -> new NotFoundException(
                STATION_NOT_FOUND,
                STATION_NOT_FOUND.getMessage() + " id = " + id));
        return Station.create(stationEntity.getName());
    }

    @Override
    public void updateById(final Long id, final Station station) {
        final StationEntity stationEntity = new StationEntity(id, station.name().name());
        final int updatedCount = stationDao.update(stationEntity);
        if (updatedCount != 1) {
            throw new DBException(DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(final Long id) {
        final int deletedCount = stationDao.deleteById(id);
        if (deletedCount != 1) {
            throw new DBException(DB_DELETE_ERROR);
        }
    }

    @Override
    public boolean existByName(final String name) {
        return stationDao.existByName(name);
    }
}
