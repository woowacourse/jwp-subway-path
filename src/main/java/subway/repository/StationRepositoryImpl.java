package subway.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

@RequiredArgsConstructor
@Repository
public class StationRepositoryImpl implements StationRepository {

    private final StationDao stationDao;

    @Override
    public Station save(final Station station) {
        final StationEntity result = stationDao.insert(StationEntity.from(station));
        return new Station(result.getId(), result.getName());
    }

    @Override
    public Optional<Station> findByName(final String name) {
        return stationDao.findByName(name)
                .map(StationEntity::toStation);
    }

    @Override
    public void delete(final Station existStation) {
        stationDao.delete(existStation.getId());
    }
}
