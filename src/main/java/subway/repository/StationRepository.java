package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.entity.StationEntity;
import subway.exception.common.NotFoundStationException;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(final Station insertStation, final Long lineId) {
        StationEntity insertedStation = stationDao.insert(new StationEntity(insertStation.getName(), lineId));
        return new Station(insertedStation.getId(), insertedStation.getName());
    }

    public Station findByNameAndLineId(String baseStation, Long lineId) {
        StationEntity stationEntity = stationDao.findByNameAndLineId(baseStation, lineId).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Station findById(Long stationId) {
        StationEntity stationEntity = stationDao.findById(stationId).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    public Station findByName(final String station, final String lineName) {
        StationEntity stationEntity = stationDao.findByName(station, lineName).orElseThrow(NotFoundStationException::new);
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
