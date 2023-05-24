package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.exeption.InvalidStationException;

import java.util.Optional;

@Repository
public class DbStationRepository implements StationRepository {
    private final StationDao stationDao;

    public DbStationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Station save(final Station station) {
        final StationEntity stationEntity = new StationEntity(station.getName());
        return Station.from(stationDao.insert(stationEntity));
    }

    @Override
    public Station findById(final Long id) {
        final Optional<StationEntity> station = stationDao.findBy(id);
        if (station.isEmpty()) {
            throw new InvalidStationException("해당 아이디의 역이 존재하지 않습니다");
        }
        return Station.from(station.get());
    }
}
