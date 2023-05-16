package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.application.domain.Station;
import subway.application.repository.StationRepository;
import subway.persistence.dao.StationDao;
import subway.persistence.row.StationRow;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class H2StationRepository implements StationRepository {

    private final StationDao stationDao;

    public H2StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station insert(Station station) {
        StationRow row = stationDao.insert(new StationRow(station.getId(), station.getName()));
        return new Station(row.getId(), row.getName());
    }

    public List<Station> findAll() {
        List<StationRow> rows = stationDao.selectAll();

        return rows.stream()
                .map(row -> new Station(row.getId(), row.getName()))
                .collect(Collectors.toList());
    }

    public Station findById(Long id) {
        StationRow row = stationDao.selectById(id);
        return new Station(row.getId(), row.getName());
    }

    public void update(Station newStation) {
        stationDao.update(new StationRow(newStation.getId(), newStation.getName()));
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
