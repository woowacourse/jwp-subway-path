package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.vo.Distance;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void saveAll(final List<SectionEntity> sectionEntities) {
        sectionDao.insertBatch(sectionEntities);
    }

    public Section findBySectionId(final Long sectionId) {
        final SectionEntity section = getSectionOrThrowException(sectionId);
        final StationEntity upStationEntity = getStationOrThrowException(section.getUpStationId());
        final StationEntity downStationEntity = getStationOrThrowException(section.getDownStationId());

        final Station upStation = upStationEntity.toDomain();
        final Station downStation = downStationEntity.toDomain();

        final Distance distance = Distance.from(section.getDistance());
        final Boolean isStart = section.getStart();

        return new Section(sectionId, distance, isStart, upStation, downStation);
    }

    private StationEntity getStationOrThrowException(final Long section) {
        return stationDao.findByStationId(section)
                .orElseThrow(() -> new IllegalArgumentException("해당 역_식별자값으로 구간을 조회하지 못했습니다."));
    }

    private SectionEntity getSectionOrThrowException(final Long sectionId) {
        return sectionDao.findBySectionId(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구간_식별자값으로 구간을 조회하지 못했습니다."));
    }

    public void deleteByLineId(final Long lineId) {
        sectionDao.deleteByLineId(lineId);
    }
}
