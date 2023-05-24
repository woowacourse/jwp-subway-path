package subway.persistence.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.station.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@Repository
public class StationRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationRepository(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Station insert(final Station station) {
        if (stationDao.existsByName(station.getName())) {
            throw new IllegalArgumentException("지정한 역의 이름은 이미 존재하는 이름입니다.");
        }

        final StationEntity stationEntity = StationEntity.from(station);
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);

        return Station.of(insertedStationEntity.getId(), insertedStationEntity.getName());
    }

    public Optional<Station> findById(final Long id) {
        return stationDao.findById(id)
                .map(StationEntity::to);
    }

    public Map<Long, Station> findAllByIds(final Set<Long> ids) {
        return stationDao.findAllByIds(ids)
                .stream()
                .collect(Collectors.toMap(StationEntity::getId, StationEntity::to));
    }

    public void deleteById(final Long id) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByStationId(id);

        if (!sectionEntities.isEmpty()) {
            throw new IllegalArgumentException("노선에 등록되어 있는 역은 삭제할 수 없습니다.");
        }

        stationDao.deleteById(id);
    }
}
