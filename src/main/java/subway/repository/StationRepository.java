package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.entity.StationEntity;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long insert(final Station station) {
        return stationDao.insert(StationEntity.from(station));
    }

    public List<Station> findAll() {
        return stationDao.findAll().stream()
                .map(it -> new Station(it.getId(), new StationName(it.getName())))
                .collect(Collectors.toUnmodifiableList());
    }

    public Station findById(final Long id) {
        final StationEntity stationEntity = stationDao.findById(id).orElseThrow(
                () -> new IllegalArgumentException(id + "id를 가진 역을 찾을 수 없습니다.")
        );

        return new Station(id, new StationName(stationEntity.getName()));
    }

    public void update(final Station station) {
        stationDao.update(StationEntity.from(station));
    }

    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }
}
