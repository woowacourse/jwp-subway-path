package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.dao.v2.SectionDaoV2;
import subway.dao.v2.StationDaoV2;
import subway.domain.Distance;
import subway.domain.SectionDomain;
import subway.domain.StationDomain;

import java.util.List;

@Repository
public class SectionRepository {

    private final SectionDaoV2 sectionDao;
    private final StationDaoV2 stationDao;

    public SectionRepository(final SectionDaoV2 sectionDao, final StationDaoV2 stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long save(final SectionEntity sectionEntity) {
        return sectionDao.insert(
                sectionEntity.getUpStationId(),
                sectionEntity.getDownStationId(),
                sectionEntity.getLineId(),
                sectionEntity.getStart(),
                sectionEntity.getDistance()
        );
    }

    public void saveAll(final List<SectionEntity> sectionEntities) {
        sectionDao.insertBatch(sectionEntities);
    }

    public SectionDomain findBySectionId(final Long sectionId) {
        final SectionEntity section = sectionDao.findBySectionId(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구간_식별자값으로 구간을 조회하지 못했습니다."));

        final StationEntity upStationEntity = stationDao.findByStationId(section.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 역_식별자값으로 구간을 조회하지 못했습니다."));

        final StationEntity downStationEntity = stationDao.findByStationId(section.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 역_식별자값으로 구간을 조회하지 못했습니다."));

        final StationDomain upStation = new StationDomain(upStationEntity.getId(), upStationEntity.getName());
        final StationDomain downStation = new StationDomain(downStationEntity.getId(), downStationEntity.getName());

        final Distance distance = new Distance(section.getDistance());
        final Boolean isStart = section.getStart();

        return new SectionDomain(sectionId, distance, isStart, upStation, downStation);
    }

    public void deleteByLineId(final Long lineId) {
        sectionDao.deleteByLineId(lineId);
    }
}
