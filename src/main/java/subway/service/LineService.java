package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.LineSections;
import subway.service.dto.LineDto;
import subway.service.dto.SectionCreateDto;
import subway.exception.DuplicatedStationNameException;
import subway.repository.LineDao;
import subway.repository.SectionDao;
import subway.repository.StationDao;
import subway.entity.LineEntity;
import subway.entity.SectionDetailEntity;
import subway.entity.SectionEntity;
import subway.controller.dto.request.StationRegisterInLineRequest;
import subway.controller.dto.request.StationUnregisterInLineRequest;
import subway.controller.dto.request.converter.SubwayDirection;
import subway.controller.dto.response.LineResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private static final int SIZE_OF_END_POINT_STATION = 1;

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

    @Transactional
    public LineResponse registerStation(final Long lineId, final StationRegisterInLineRequest request) {
        final long standardStationId = stationDao.findIdByName(request.getStandardStationName());
        final long newStationId = stationDao.findIdByName(request.getNewStationName());
        validateNewStationNotExistInLine(lineId, newStationId);
        final int distance = request.getDistance();
        if (request.getDirection() == SubwayDirection.UP) {
            final List<SectionDetailEntity> registeredUpperEntity = registerUpperStation(new SectionEntity(lineId, distance, standardStationId, newStationId));
            return convertToResponse(registeredUpperEntity);
        }
        final List<SectionDetailEntity> registeredDownEntity = registerDownStation(new SectionEntity(lineId, distance, newStationId, standardStationId));
        return convertToResponse(registeredDownEntity);
    }

    private void validateNewStationNotExistInLine(final long lineId, final long stationId) {
        if (sectionDao.findByLineIdAndPreviousStationIdOrNextStationId(lineId, stationId).isPresent()) {
            throw new DuplicatedStationNameException();
        }
    }

    private List<SectionDetailEntity> registerUpperStation(final SectionEntity newSectionEntity) {
        final Optional<SectionEntity> baseSection =
                sectionDao.findByLineIdAndPreviousStationId(newSectionEntity.getLineId(), newSectionEntity.getPreviousStationId());
        return baseSection.map(sectionEntity -> registerUpperMidStation(sectionEntity, newSectionEntity))
                .orElseGet(() -> registerEndStation(newSectionEntity));
    }

    private List<SectionDetailEntity> registerUpperMidStation(final SectionEntity base, final SectionEntity newPrevious) {
        final SectionEntity newNext = base.getSplitNextBy(newPrevious);
        return mergeSections(base, newPrevious, newNext);
    }

    private List<SectionDetailEntity> mergeSections(final SectionEntity base, final SectionEntity newPrevious, final SectionEntity newNext) {
        sectionDao.delete(base);
        sectionDao.insert(newPrevious);
        sectionDao.insert(newNext);
        return sectionDao.findSectionDetailByLineId(base.getLineId());
    }

    private List<SectionDetailEntity> registerEndStation(final SectionEntity newSectionEntity) {
        sectionDao.insert(newSectionEntity);
        return sectionDao.findSectionDetailByLineId(newSectionEntity.getLineId());
    }

    private List<SectionDetailEntity> registerDownStation(final SectionEntity newSectionEntity) {
        Optional<SectionEntity> baseSection =
                sectionDao.findByLineIdAndNextStationId(newSectionEntity.getLineId(), newSectionEntity.getNextStationId());
        return baseSection.map(sectionEntity -> registerDownMidStation(sectionEntity, newSectionEntity))
                .orElseGet(() -> registerEndStation(newSectionEntity));
    }

    private List<SectionDetailEntity> registerDownMidStation(final SectionEntity base, final SectionEntity newNext) {
        final SectionEntity newPrevious = base.getSplitPreviousBy(newNext);
        return mergeSections(base, newPrevious, newNext);
    }

    @Transactional
    public Optional<LineResponse> unregisterStation(final Long lineId, final StationUnregisterInLineRequest request) {
        final List<SectionEntity> relatedSectionEntities = sectionDao.findByLineIdAndPreviousStationNameOrNextStationName(lineId, request.getStationName());
        if (relatedSectionEntities.size() == SIZE_OF_END_POINT_STATION) {
            final List<SectionDetailEntity> sectionDetailEntities = unregisterEndStation(relatedSectionEntities.get(0));
            return Optional.of(convertToResponse(sectionDetailEntities));
        }
        final List<SectionDetailEntity> sectionDetailEntities = unregisterMidStation(relatedSectionEntities.get(0), relatedSectionEntities.get(1));
        return Optional.of(convertToResponse(sectionDetailEntities));
    }

    private List<SectionDetailEntity> unregisterEndStation(final SectionEntity sectionEntity) {
        final long lineId = sectionEntity.getLineId();
        sectionDao.delete(sectionEntity);
        final List<SectionDetailEntity> sectionDetailEntities = sectionDao.findSectionDetailByLineId(lineId);
        deleteLineIfSectionNonExist(lineId, sectionDetailEntities);
        return sectionDetailEntities;
    }

    private void deleteLineIfSectionNonExist(final long lineId, final List<SectionDetailEntity> sectionDetailEntities) {
        if (sectionDetailEntities.isEmpty()) {
            lineDao.deleteById(lineId);
        }
    }

    private List<SectionDetailEntity> unregisterMidStation(final SectionEntity previous, final SectionEntity next) {
        sectionDao.deleteAll(List.of(previous, next));
        final SectionEntity boundSectionEntity = SectionEntity.createBoundOf(previous, next);
        sectionDao.insert(boundSectionEntity);
        return sectionDao.findSectionDetailByLineId(previous.getLineId());
    }
}
