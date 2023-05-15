package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(Station station) {
        StationEntity savedStationEntity = stationDao.insert(new StationEntity(null, station.getName()));
        return Station.from(savedStationEntity);
    }

    public Station findById(Long id) {
        StationEntity stationEntity = stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        return Station.from(stationEntity);
    }

    public Station findByName(String name) {
        StationEntity stationEntity = stationDao.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));

        return Station.from(stationEntity);
    }

    public List<Station> findAll() {
        return stationDao.findAll().stream()
                .map(Station::from)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
