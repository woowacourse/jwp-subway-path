package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.SectionEntity;
import subway.service.converter.LineConverter;
import subway.service.dto.LineDto;
import subway.service.dto.SectionCreateDto;

@Service
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public long save(final LineDto lineDto, final SectionCreateDto sectionCreateDto) {
        final Long lineId = lineDao.insert(LineConverter.toEntity(lineDto));
        final Long previousStationId = stationDao.findIdByName(sectionCreateDto.getPreviousStationName());
        final Long nextStationId = stationDao.findIdByName(sectionCreateDto.getNextStationName());
        sectionDao.insert(new SectionEntity.Builder()
                .lineId(lineId)
                .distance(sectionCreateDto.getDistance())
                .previousStationId(previousStationId)
                .nextStationId(nextStationId)
                .build());
        return lineId;
    }
}
