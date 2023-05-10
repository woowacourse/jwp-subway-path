package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.SectionInsertDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.SectionEntity;

@Service
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public long save(final SectionInsertDto sectionInsertDto) {
        // lineName 으로 line 조회
        // station 을 불러오는데, Id 값을 가져와야함

        // 그리고 Section 으로 묶어서 저장

        final Long lineId = lineDao.findIdByName(sectionInsertDto.getLineName());
        final Long standardStationId = stationDao.findIdByName(sectionInsertDto.getStandardStationName());
        final Long additionStationId = stationDao.findIdByName(sectionInsertDto.getAdditionalStationName());
        new SectionEntity.Builder()
                .lineId(lineId).previousStationId(standardStationId).nextStationId(additionStationId)
        return 1L;
    }

    private long saveUpperSection(final Long lindId, final Long previousStationId,
                                  final Long additionalStationId, final int distance) {
        sectionDao.findByLineIdAndPreviousStationId(lineId, previousStationId);
    }

}
