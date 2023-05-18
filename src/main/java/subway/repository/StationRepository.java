package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.entity.StationEntity;
import subway.repository.converter.StationConverter;
import subway.service.domain.Station;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(Station station) {
        StationEntity stationEntity = StationConverter.domainToEntity(station);
        Long id = stationDao.insert(stationEntity);

        return StationConverter.entityToDomain(id, stationEntity);
    }

}
