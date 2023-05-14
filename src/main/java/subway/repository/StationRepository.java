package subway.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.entity.StationEntity;

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
                         .map(stationEntity -> new Station(stationEntity.getName()));
    }

    public Optional<Station> findStationById(long stationId) {
        return stationDao.findById(stationId)
                         .map(stationEntity -> new Station(stationEntity.getName()));
    }

    public long createStation(Station stationToInsert) {
        stationDao.findByName(stationToInsert.getName())
                  .ifPresent(ignored -> {throw new IllegalStateException("디버깅: 추가하려는 역이 이미 존재합니다");});

        final StationEntity stationEntity = new StationEntity.Builder().name(stationToInsert.getName()).build();
        return stationDao.insert(stationEntity).getId();

    }

    public Optional<Long> findIdByName(String stationName) {
        return stationDao.findIdByName(stationName);
    }
}
