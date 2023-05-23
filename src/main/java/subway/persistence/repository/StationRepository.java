package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.application.request.StationRequest;
import subway.domain.station.Station;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public long saveSection(final StationRequest request) {
        final StationEntity station = new StationEntity(request.getName());
        return stationDao.insert(station);
    }

    public Station findStationById(final Long id) {
        final StationEntity stationEntity = stationDao.findById(id);
        return stationEntity.mapToStation();
    }

    public List<Station> findAllStations() {
        final List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(StationEntity::mapToStation)
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        final StationEntity station = new StationEntity(id, stationRequest.getName());
        stationDao.update(station);
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
