package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.response.LineResponse;
import subway.domain.LineSections;
import subway.entity.LineEntity;
import subway.entity.SectionDetailEntity;
import subway.entity.SectionEntity;
import subway.repository.LineDao;
import subway.repository.SectionDao;
import subway.repository.StationDao;
import subway.service.dto.LineDto;
import subway.service.dto.SectionCreateDto;

import java.util.List;
import java.util.stream.Collectors;

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
    public long create(final LineDto lineDto, final SectionCreateDto sectionCreateDto) {
        final LineEntity lineEntity = lineDao.insert(new LineEntity(lineDto.getName(), lineDto.getColor()));
        final long previousStationId = stationDao.findIdByName(sectionCreateDto.getFirstStation());
        final long nextStationId = stationDao.findIdByName(sectionCreateDto.getLastStation());
        sectionDao.insert(new SectionEntity(lineEntity.getId(), sectionCreateDto.getDistance(), previousStationId, nextStationId));
        return lineEntity.getId();
    }

    public List<LineResponse> findAll() {
        return sectionDao.findSectionDetail().stream()
                .collect(Collectors.groupingBy(SectionDetailEntity::getLineId))
                .values().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findById(final Long lineId) {
        final List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionDetailByLineId(lineId);
        return convertToResponse(sectionDetailEntities);
    }

    private LineResponse convertToResponse(final List<SectionDetailEntity> entities) {
        final LineSections lineSections = LineSections.createByEntities(entities);
        return LineResponse.createByDomain(lineSections);
    }
}
