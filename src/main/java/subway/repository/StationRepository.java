package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station findById(final Long id) {
        Optional<StationEntity> maybeStationEntity = stationDao.findById(id);
        if (maybeStationEntity.isEmpty()) {
            throw new IllegalArgumentException("해당 역은 존재하지 않습니다");
        }
        return maybeStationEntity.get().convertToStation();
    }

    public Station save(final Station station) {
        StationEntity stationEntity = StationEntity.from(station);
        Long id = stationDao.save(stationEntity);
        return new Station(
                id,
                station.getName()
        );
    }

    public void updateByLine(final Line beforeLine, final Line updateLine) {
        List<Station> beforeStations = beforeLine.findAllStation();
        beforeStations.removeAll(updateLine.findAllStation());
        List<StationEntity> beforeStationEntities = convertToStationEntities(beforeStations);
        stationDao.delete(beforeStationEntities);

        List<Station> updateStations = updateLine.findAllStation();
        updateStations.removeAll(beforeLine.findAllStation());
        List<StationEntity> updateStationEntities = convertToStationEntities(updateStations);
        stationDao.save(updateStationEntities);
    }

    public void update(final Station before, final Station after) {
        StationEntity stationEntity = new StationEntity(before.getId(), after.getName());
        stationDao.update(stationEntity);
    }

    public void delete(final Station station) {
        stationDao.deleteById(station.getId());
    }

    private List<StationEntity> convertToStationEntities(final List<Station> stations) {
        return stations.stream()
                .map(StationEntity::from)
                .collect(Collectors.toList());
    }
}
