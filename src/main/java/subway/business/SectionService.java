package subway.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.business.converter.section.SectionDetailEntityResponseConverter;
import subway.business.dto.SectionInsertDto;
import subway.exception.bad_request.InvalidDistanceException;
import subway.exception.not_found.StationNotFoundException;
import subway.persistence.SectionDao;
import subway.persistence.StationDao;
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.SectionEntity;
import subway.presentation.dto.response.SectionResponse;
import subway.presentation.query_option.SubwayDirection;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(final StationDao stationDao, final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public List<SectionResponse> save(final SectionInsertDto sectionInsertDto) {
        final List<SectionEntity> sectionEntities = sectionDao.findByLineName(sectionInsertDto.getLineName());
        final long standardStationId = stationDao.findIdByName(sectionInsertDto.getStandardStationName());
        final long newStationId = stationDao.findIdByName(sectionInsertDto.getNewStationName());
        final int distance = sectionInsertDto.getDistance();
        final StationLocation standardStationLocation = findStandardStationLocation(sectionEntities, standardStationId);
        if (sectionInsertDto.getDirection() == SubwayDirection.UP) {
            return saveUpperSection(sectionEntities, standardStationLocation, standardStationId, newStationId, distance);
        }
        return saveDownSection(sectionEntities, standardStationLocation, standardStationId, newStationId, distance);
    }

    private StationLocation findStandardStationLocation(final List<SectionEntity> sectionEntities,
                                                        final Long standardStationId) {
        final List<Long> previousStationIds = sectionEntities.stream()
                .map(SectionEntity::getPreviousStationId)
                .collect(Collectors.toUnmodifiableList());
        final List<Long> nextStationIds = sectionEntities.stream()
                .map(SectionEntity::getNextStationId)
                .collect(Collectors.toUnmodifiableList());
        if (!previousStationIds.contains(standardStationId) && nextStationIds.contains(standardStationId)) {
            return StationLocation.TOP;
        }
        if (previousStationIds.contains(standardStationId) && !nextStationIds.contains(standardStationId)) {
            return StationLocation.BOTTOM;
        }
        if (previousStationIds.contains(standardStationId) && nextStationIds.contains(standardStationId)) {
            return StationLocation.MID;
        }
        throw new StationNotFoundException();
    }

    private List<SectionResponse> saveUpperSection(final List<SectionEntity> sectionEntities,
                                                   final StationLocation standardStationLocation,
                                                   final long standardStationId,
                                                   final long newStationId,
                                                   final int distance) {
        final long lineId = sectionEntities.get(0).getLineId();
        if (standardStationLocation == StationLocation.TOP) {
            return saveEndStation(new SectionEntity(lineId, distance, standardStationId, newStationId));
        }
        final SectionEntity originalSectionEntity = findSectionEntityByPreviousStationId(sectionEntities, standardStationId);
        validateDistance(distance, originalSectionEntity.getDistance());
        final SectionEntity frontSectionEntity = new SectionEntity(lineId, distance, standardStationId, newStationId);
        final SectionEntity backSectionEntity = new SectionEntity(
                lineId, originalSectionEntity.getDistance() - distance,
                newStationId, originalSectionEntity.getNextStationId());
        return saveBetweenStations(frontSectionEntity, backSectionEntity, originalSectionEntity);
    }

    private SectionEntity findSectionEntityByPreviousStationId(final List<SectionEntity> sectionEntities,
                                                               final long previousStationId) {
        return sectionEntities.stream()
                .filter(sectionEntity -> sectionEntity.getPreviousStationId() == previousStationId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("station id로 section entity 가 검색되지 않습니다."));
    }

    private List<SectionResponse> saveDownSection(final List<SectionEntity> sectionEntities,
                                                  final StationLocation standardStationLocation,
                                                  final long standardStationId,
                                                  final long newStationId,
                                                  final int distance) {
        final long lineId = sectionEntities.get(0).getLineId();
        if (standardStationLocation == StationLocation.BOTTOM) {
            return saveEndStation(new SectionEntity(lineId, distance, newStationId, standardStationId));
        }
        final SectionEntity originalSectionEntity = findSectionEntityByNextStationId(sectionEntities, standardStationId);
        validateDistance(distance, originalSectionEntity.getDistance());
        final SectionEntity frontSectionEntity = new SectionEntity(
                lineId, originalSectionEntity.getDistance() - distance,
                originalSectionEntity.getPreviousStationId(), newStationId);
        final SectionEntity backSectionEntity = new SectionEntity(lineId, distance, newStationId, standardStationId);
        return saveBetweenStations(frontSectionEntity, backSectionEntity, originalSectionEntity);
    }

    private SectionEntity findSectionEntityByNextStationId(final List<SectionEntity> sectionEntities,
                                                           final long nextStationId) {
        return sectionEntities.stream()
                .filter(sectionEntity -> sectionEntity.getNextStationId() == nextStationId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("station id로 section entity 가 검색되지 않습니다."));
    }

    private void validateDistance(final int newDistance, final int originalDistance) {
        if (newDistance >= originalDistance) {
            throw new InvalidDistanceException();
        }
    }

    private List<SectionResponse> saveEndStation(final SectionEntity sectionEntity) {
        final SectionEntity insertedSectionEntity = sectionDao.insert(sectionEntity);
        final SectionDetailEntity sectionDetailEntity = sectionDao.findSectionDetailById(insertedSectionEntity.getId());
        return SectionDetailEntityResponseConverter.toResponses(List.of(sectionDetailEntity));
    }

    private List<SectionResponse> saveBetweenStations(final SectionEntity frontSectionEntity,
                                                      final SectionEntity backSectionEntity,
                                                      final SectionEntity originalSectionEntity) {
        final SectionEntity insertedFrontSectionEntity = sectionDao.insert(frontSectionEntity);
        final SectionDetailEntity frontSectionDetailEntity =
                sectionDao.findSectionDetailById(insertedFrontSectionEntity.getId());
        final SectionEntity insertedBackSectionEntity = sectionDao.insert(backSectionEntity);
        final SectionDetailEntity backSectionDetailEntity =
                sectionDao.findSectionDetailById(insertedBackSectionEntity.getId());
        sectionDao.delete(originalSectionEntity);
        return SectionDetailEntityResponseConverter.toResponses(
                List.of(frontSectionDetailEntity, backSectionDetailEntity));
    }

    private enum StationLocation {
        TOP, MID, BOTTOM
    }
}
