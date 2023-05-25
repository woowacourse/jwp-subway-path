package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dao.StationEntity;
import subway.domain.station.Station;

@Repository
public class StationRepository {

    private final StationDao stationDao;
    private final Mapper mapper;

    public StationRepository(final StationDao stationDao, final Mapper mapper) {
        this.stationDao = stationDao;
        this.mapper = mapper;
    }

    public Station save(final Station station) {
        final StationEntity stationEntity = stationDao.insert(mapper.toStationEntity(station));
        return mapper.toStation(stationEntity);
    }

    public Station findStationById(final Long id) {
        final StationEntity stationEntity = stationDao.findById(id);
        return mapper.toStation(stationEntity);
    }

    public boolean contains(final Station station) {
        return stationDao.findAll().stream()
                .anyMatch(it -> it.getName().equals(station.getName()));
    }
}
