package subway.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.converter.line.LineSectionsResponseConverter;
import subway.business.converter.section.SectionDetailEntityDomainConverter;
import subway.business.domain.LineSections;
import subway.business.dto.LineDto;
import subway.business.dto.SectionCreateDto;
import subway.exception.bad_request.DuplicatedStationNameException;
import subway.exception.bad_request.InvalidDistanceException;
import subway.exception.not_found.LineNotFoundException;
import subway.persistence.LineDao;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.SectionEntity;
import subway.presentation.dto.request.StationRegisterInLineRequest;
import subway.presentation.dto.request.StationUnregisterInLineRequest;
import subway.presentation.dto.request.converter.SubwayDirection;
import subway.presentation.dto.response.LineResponse;

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
    public long createLineWithSection(final LineDto lineDto, final SectionCreateDto sectionCreateDto) {
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
        final LineSections lineSections = SectionDetailEntityDomainConverter.toLineSections(entities);
        return LineSectionsResponseConverter.toResponse(lineSections);
    }

    @Transactional
    public LineResponse registerStation(final Long lineId, final StationRegisterInLineRequest request) {
        final long standardStationId = stationDao.findIdByName(request.getStandardStationName());
        final long newStationId = stationDao.findIdByName(request.getNewStationName());
        validateNewStationNotExistInLine(lineId, newStationId);
        final int distance = request.getDistance();
        if (request.getDirection() == SubwayDirection.UP) {
            final List<SectionDetailEntity> registeredUpperEntity =
                    registerUpperStation(new SectionEntity(lineId, distance, standardStationId, newStationId));
            return convertToResponse(registeredUpperEntity);
        }
        final List<SectionDetailEntity> registeredDownEntity =
                registerDownStation(new SectionEntity(lineId, distance, newStationId, standardStationId));
        return convertToResponse(registeredDownEntity);
    }

    private void validateNewStationNotExistInLine(final long lineId, final long stationId) {
        if (sectionDao.findByLineIdAndPreviousStationIdOrNextStationId(lineId, stationId).isPresent()) {
            throw new DuplicatedStationNameException();
        }
    }

    private List<SectionDetailEntity> registerUpperStation(final SectionEntity newSectionEntity) {
        Optional<SectionEntity> baseSection =
                sectionDao.findByLineIdAndPreviousStationId(newSectionEntity.getLineId(), newSectionEntity.getPreviousStationId());
        return baseSection.map(sectionEntity -> registerUpperMidStation(sectionEntity, newSectionEntity))
                .orElseGet(() -> registerEndStation(newSectionEntity));
    }

    private List<SectionDetailEntity> registerEndStation(final SectionEntity newSectionEntity) {
        sectionDao.insert(newSectionEntity);
        return sectionDao.findSectionDetailByLineId(newSectionEntity.getLineId());
    }

    private List<SectionDetailEntity> registerUpperMidStation(final SectionEntity base, final SectionEntity newPrevious) {
        validateDistance(base.getDistance(), newPrevious.getDistance());
        final int newNextDistance = base.getDistance() - newPrevious.getDistance();
        final SectionEntity newNext = new SectionEntity(base.getLineId(), newNextDistance,
                newPrevious.getNextStationId(), base.getNextStationId());
        return mergeSections(base, newPrevious, newNext);
    }

    private List<SectionDetailEntity> mergeSections(final SectionEntity base, final SectionEntity newPrevious, final SectionEntity newNext) {
        sectionDao.delete(base);
        sectionDao.insert(newPrevious);
        sectionDao.insert(newNext);
        return sectionDao.findSectionDetailByLineId(base.getLineId());
    }

    private List<SectionDetailEntity> registerDownStation(final SectionEntity newSectionEntity) {
        Optional<SectionEntity> baseSection =
                sectionDao.findByLineIdAndNextStationId(newSectionEntity.getLineId(), newSectionEntity.getNextStationId());
        return baseSection.map(sectionEntity -> registerDownMidStation(sectionEntity, newSectionEntity))
                .orElseGet(() -> registerEndStation(newSectionEntity));
    }

    private List<SectionDetailEntity> registerDownMidStation(final SectionEntity base, final SectionEntity newNext) {
        validateDistance(base.getDistance(), newNext.getDistance());
        final int newPreviousDistance = base.getDistance() - newNext.getDistance();
        final SectionEntity newPrevious = new SectionEntity(base.getLineId(), newPreviousDistance,
                base.getPreviousStationId(), newNext.getPreviousStationId());
        return mergeSections(base, newPrevious, newNext);
    }

    private void validateDistance(final int originalDistance, final int newDistance) {
        if (originalDistance <= newDistance) {
            throw new InvalidDistanceException();
        }
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