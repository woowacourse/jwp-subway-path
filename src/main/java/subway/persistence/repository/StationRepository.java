package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private static final int ZERO = 0;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public StationRepository(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public Station insert(final Station station) {
        final StationEntity stationEntity = StationEntity.from(station);
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);

        return Station.of(insertedStationEntity.getId(), insertedStationEntity.getName());
    }

    public List<Station> findAll() {
        return stationDao.findAll().stream()
                .map(StationEntity::toDomain)
                .collect(Collectors.toList());
    }

    public Station findById(final Long id) {
        return stationDao.findById(id).toDomain();
    }

    public void deleteById(final Long id) {
        validateHasStationInLine(id);

        final int count = stationDao.deleteById(id);

        if (count == ZERO) {
            throw new IllegalArgumentException("등록되지 않은 역입니다.");
        }
    }

    private void validateHasStationInLine(final Long id) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByStationId(id);

        if (!sectionEntities.isEmpty()) {
            throw new IllegalArgumentException("노선에 추가되어 있는 역이라 삭제할 수 없습니다");
        }
    }
}
