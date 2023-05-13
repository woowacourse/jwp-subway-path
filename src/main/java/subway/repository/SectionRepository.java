package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.dao.v2.SectionDaoV2;
import subway.dao.v2.StationDaoV2;
import subway.domain.Distance;
import subway.domain.SectionDomain;
import subway.domain.StationDomain;

@Repository
public class SectionRepository {

    private final SectionDaoV2 sectionDao;
    private final StationDaoV2 stationDao;

    public SectionRepository(final SectionDaoV2 sectionDao, final StationDaoV2 stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public SectionDomain findBySectionId(final Long sectionId) {
        final SectionEntity section = sectionDao.findBySectionId(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구간_식별자값으로 구간을 조회하지 못했습니다."));

        final StationDomain upStation = stationDao.findByStationId(section.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 역_식별자값으로 구간을 조회하지 못했습니다."));

        final StationDomain downStation = stationDao.findByStationId(section.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 역_식별자값으로 구간을 조회하지 못했습니다."));

        final Distance distance = new Distance(section.getDistance());
        final Boolean isStart = section.getStart();

        return SectionDomain.from(sectionId, distance, isStart, upStation, downStation);
    }
}
