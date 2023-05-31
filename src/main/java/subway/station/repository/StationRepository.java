package subway.station.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.station.dao.StationDao;
import subway.station.domain.Station;
import subway.station.entity.StationEntity;

import java.util.Optional;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    @Autowired
    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }


    public Optional<Station> findStationByName(String stationName) {
        return stationDao.findByName(stationName)
                         .map(EntityMapper::toDomain);
    }

    public Optional<Station> findStationById(long stationId) {
        return stationDao.findById(stationId)
                         .map(EntityMapper::toDomain);
    }

    public Station createStation(Station stationToInsert) {
        stationDao.findByName(stationToInsert.getName())
                  .ifPresent(ignored -> {
                      throw new IllegalStateException("디버깅: 추가하려는 역이 이미 존재합니다");
                  });

        final StationEntity stationEntity = EntityMapper.toEntity(stationToInsert);
        return EntityMapper.toDomain(stationDao.insert(stationEntity));
    }
}
