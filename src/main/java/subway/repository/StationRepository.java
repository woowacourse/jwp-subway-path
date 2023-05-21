package subway.repository;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.exception.station.StationNotFoundException;
import subway.repository.dao.StationDao;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station insert(String stationName) {
        if (stationDao.existsByName(stationName)) {
            final StationEntity findStation = stationDao.findByName(stationName)
                    .orElseThrow(() -> new StationNotFoundException("해당 이름을 가진 역이 존재하지 않습니다"));
            return findStation.toDomain();
        }
        final StationEntity saveStation = stationDao.insert(new StationEntity(stationName));
        return new Station(saveStation.getId(), saveStation.getName());
    }

    public Optional<Station> findById(Long stationId) {
        return stationDao.findById(stationId)
                .map(StationEntity::toDomain);
    }

    public void deleteById(final Long stationId) {
        stationDao.deleteById(stationId);
    }
}
