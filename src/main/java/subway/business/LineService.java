package subway.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.converter.line.LineSectionsResponseConverter;
import subway.business.converter.section.SectionDetailEntityDomainConverter;
import subway.business.domain.LineSections;
import subway.business.dto.LineDto;
import subway.business.dto.SectionCreateDto;
import subway.exception.not_found.LineNotFoundException;
import subway.persistence.LineDao;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;
import subway.presentation.dto.request.StationDeleteInLineRequest;
import subway.presentation.dto.response.LineDetailResponse;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private static final int IS_END_STATION = 1;

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
        final LineEntity lineEntity = lineDao.insert(new LineEntity(lineDto.getName(), lineDto.getColor()));
        final StationEntity previousStation = stationDao.findByName(sectionCreateDto.getFirstStation());
        final StationEntity nextStation = stationDao.findByName(sectionCreateDto.getLastStation());
        sectionDao.insert(new SectionEntity(
                lineEntity.getId(), sectionCreateDto.getDistance(),
                previousStation.getId(), nextStation.getId())
        );
        return lineEntity.getId();
    }

    public List<LineDetailResponse> findAll() {
        return sectionDao.findSectionDetail().stream()
                .collect(Collectors.groupingBy(SectionDetailEntity::getLineId))
                .values().stream()
                .map(this::sortAndReturnToResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineDetailResponse findById(final Long lineId) {
        return sortAndReturnToResponse(sectionDao.findSectionDetailByLineId(lineId));
    }

    private LineDetailResponse sortAndReturnToResponse(final List<SectionDetailEntity> entities) {
        final LineSections lineSections = SectionDetailEntityDomainConverter.toLineSections(entities);
        return LineSectionsResponseConverter.toResponse(lineSections);
    }

    @Transactional
    public Optional<LineDetailResponse> deleteStation(final Long lineId, final StationDeleteInLineRequest request) {
        final List<SectionEntity> relatedSectionEntities =
                sectionDao.findByLineIdAndPreviousStationNameOrNextStationName(lineId, request.getStationName());
        sectionDao.delete(relatedSectionEntities.get(0));
        if (relatedSectionEntities.size() == IS_END_STATION) {
            return cascadeLine(lineId);
        }
        sectionDao.delete(relatedSectionEntities.get(1));
        bindSections(lineId, relatedSectionEntities);
        return Optional.of(sortAndReturnToResponse(sectionDao.findSectionDetailByLineId(lineId)));
    }

    private Optional<LineDetailResponse> cascadeLine(final long lineId) {
        try {
            return Optional.of(sortAndReturnToResponse(sectionDao.findSectionDetailByLineId(lineId)));
        } catch (LineNotFoundException e) {
            lineDao.deleteById(lineId);
            return Optional.empty();
        }
    }

    private void bindSections(final long lineId, final List<SectionEntity> relatedSectionEntities) {
        final List<SectionEntity> sectionEntities = matchOrderOfTwoSectionEntities(relatedSectionEntities);
        final SectionEntity frontEntity = sectionEntities.get(0);
        final SectionEntity backEntity = sectionEntities.get(1);
        final int distance = frontEntity.getDistance() + backEntity.getDistance();
        final SectionEntity concatSectionEntity = new SectionEntity(lineId, distance,
                frontEntity.getPreviousStationId(), backEntity.getNextStationId());
        sectionDao.insert(concatSectionEntity);
    }

    private List<SectionEntity> matchOrderOfTwoSectionEntities(final List<SectionEntity> relatedSectionEntities) {
        final SectionEntity firstEntity = relatedSectionEntities.get(0);
        final SectionEntity secondEntity = relatedSectionEntities.get(1);
        if (Objects.equals(firstEntity.getNextStationId(), secondEntity.getPreviousStationId())) {
            return List.copyOf(relatedSectionEntities);
        }
        return List.of(secondEntity, firstEntity);
    }

}
