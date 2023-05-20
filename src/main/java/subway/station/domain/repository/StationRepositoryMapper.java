package subway.station.domain.repository;

import org.springframework.stereotype.Repository;
import subway.station.domain.Station;
import subway.station.domain.entity.StationEntity;
import subway.station.exception.StationNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class StationRepositoryMapper implements StationRepository {

    private final StationDao stationDao;

    public StationRepositoryMapper(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public Long insert(final Station station) {
        return stationDao.insert(StationEntity.of(station.getId(), station.getNameValue()));
    }

    @Override
    public Station findById(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Station findFinalUpStation(final Long lineId) {
        return stationDao.findFinalUpStation(lineId)
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public Station findFinalDownStation(final Long lineId) {
        return stationDao.findFinalDownStation(lineId)
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain();
    }

    @Override
    public List<Station> findAll() {
        return stationDao.findAll().stream()
                .map(StationEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void updateById(final Long id, final Station station) {
        Station originStation = findById(id);
        originStation.updateInfo(station.getName());
        stationDao.updateById(id, StationEntity.of(originStation.getName()));
    }

    @Override
    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }

    @Override
    public Map<String, Station> getFinalStations(final Long lineId) {
        HashMap<String, Station> finalStations = new HashMap<>();
        finalStations.put("finalUpStation", stationDao.findFinalUpStation(lineId)
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain());
        finalStations.put("finalDownStation", stationDao.findFinalDownStation(lineId)
                .orElseThrow(() -> StationNotFoundException.THROW)
                .toDomain());
        return finalStations;
    }

}
