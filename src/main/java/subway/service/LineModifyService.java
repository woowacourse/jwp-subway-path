package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.StationRegisterInLineRequest;
import subway.controller.dto.request.StationUnregisterInLineRequest;
import subway.controller.dto.request.SubwayDirection;
import subway.controller.dto.response.LineResponse;
import subway.domain.LineSections;
import subway.entity.SectionDetailEntity;
import subway.entity.SectionEntity;
import subway.exception.DuplicatedStationNameException;
import subway.repository.LineDao;
import subway.repository.SectionDao;
import subway.repository.StationDao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class LineModifyService {

    private static final int END_POINT_STATION_SIZE = 1;

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineModifyService(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
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
        if (sectionDao.isStationExistInLine(lineId, stationId)) {
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
        if (relatedSectionEntities.size() == END_POINT_STATION_SIZE) {
            final List<SectionDetailEntity> sectionDetailEntities = unregisterEndStation(relatedSectionEntities.get(0));
            if (sectionDetailEntities.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(convertToResponse(sectionDetailEntities));
        }
        final List<SectionDetailEntity> sectionDetailEntities = unregisterMidStation(relatedSectionEntities.get(0), relatedSectionEntities.get(1));
        return Optional.of(convertToResponse(sectionDetailEntities));
    }

    private List<SectionDetailEntity> unregisterEndStation(final SectionEntity sectionEntity) {
        final long lineId = sectionEntity.getLineId();
        sectionDao.delete(sectionEntity);
        if (sectionDao.isSectionNotExistInLine(lineId)) {
            lineDao.deleteById(lineId);
            return Collections.emptyList();
        }
        return sectionDao.findSectionDetailByLineId(lineId);
    }

    private List<SectionDetailEntity> unregisterMidStation(final SectionEntity previous, final SectionEntity next) {
        sectionDao.deleteAll(List.of(previous, next));
        final SectionEntity boundSectionEntity = SectionEntity.createBoundOf(previous, next);
        sectionDao.insert(boundSectionEntity);
        return sectionDao.findSectionDetailByLineId(previous.getLineId());
    }

    private LineResponse convertToResponse(final List<SectionDetailEntity> entities) {
        final LineSections lineSections = LineSections.createByEntities(entities);
        return LineResponse.createByDomain(lineSections);
    }
}
