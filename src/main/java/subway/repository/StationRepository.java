package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.repository.dao.SectionDao;
import subway.repository.dao.StationDao;
import subway.repository.entity.StationEntity;

@Repository
public class StationRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public StationRepository(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Station save(Station station) {
        StationEntity stationEntity = new StationEntity(station.getName());
        StationEntity savedEntity = stationDao.insert(stationEntity);
        return toStation(savedEntity);
    }

    private Station toStation(StationEntity savedEntity) {
        return new Station(savedEntity.getId(), savedEntity.getName());
    }

    public Optional<Station> findByName(String name) {
        return stationDao.findByName(name).map(this::toStation);
    }

    public void deleteById(Long id) {
        if (sectionDao.existsByStationId(id)) {
            throw new IllegalArgumentException("구간에 저장된 역은 삭제할 수 없습니다");
        }
        stationDao.deleteByIds(List.of(id));
    }
}
