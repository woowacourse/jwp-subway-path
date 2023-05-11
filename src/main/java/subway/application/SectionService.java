package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.SectionInsertDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.ui.query_option.SubwayDirection;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Long> save(final SectionInsertDto sectionInsertDto) {
        final Long lineId = lineDao.findIdByName(sectionInsertDto.getLineName());
        final StationEntity standardStation = stationDao.findByName(sectionInsertDto.getStandardStationName());
        final StationEntity additionStation = stationDao.findByName(sectionInsertDto.getAdditionalStationName());

        if (sectionInsertDto.getDirection() == SubwayDirection.UP) {
            return saveUpperSection(lineId, standardStation.getId(), additionStation.getId(), sectionInsertDto.getDistance())
                    .stream()
                    .map(SectionEntity::getId)
                    .collect(Collectors.toUnmodifiableList());
        }

        return saveDownSection(lineId, standardStation.getId(), additionStation.getId(), sectionInsertDto.getDistance())
                .stream()
                .map(SectionEntity::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    private List<SectionEntity> saveUpperSection(final Long lineId, final Long previousStationId,
                                                 final Long additionalStationId, final int distance) {
        final List<SectionEntity> sections = sectionDao.findByLineIdAndPreviousStationId(lineId, previousStationId);

        if (sections.isEmpty()) {
            return saveLastStation(lineId, previousStationId, additionalStationId, distance);
        }

        return saveBetweenStationWhenUpper(sections, lineId, previousStationId, additionalStationId, distance);
    }

    private List<SectionEntity> saveDownSection(final Long lineId, final Long nextStationId,
                                                 final Long additionalStationId, final int distance) {
        final List<SectionEntity> sections = sectionDao.findByLineIdAndPreviousStationId(lineId, nextStationId);

        if (sections.isEmpty()) {
            return saveLastStation(lineId, additionalStationId, nextStationId, distance);
        }

        return saveBetweenStationWhenDown(sections, lineId, additionalStationId, nextStationId, distance);
    }

    private List<SectionEntity> saveBetweenStationWhenUpper(final List<SectionEntity> sections,
                                                   final Long lineId,
                                                   final Long previousStationId,
                                                   final Long nextStationId,
                                                   final int distance) {
        if (sections.size() > 1) {
            throw new RuntimeException("중복된 경로가 검색됩니다.");
        }

        final SectionEntity originalSection = sections.get(0);
        final int nextDistance = getNewDistance(distance, originalSection);
        sectionDao.delete(originalSection);

        return List.of(insertSectionEntity(lineId, previousStationId, nextStationId, distance),
                insertSectionEntity(lineId, nextStationId, originalSection.getNextStationId(), nextDistance));
    }

    private List<SectionEntity> saveBetweenStationWhenDown(final List<SectionEntity> sections,
                                                            final Long lineId,
                                                            final Long previousStationId,
                                                            final Long nextStationId,
                                                            final int distance) {
        if (sections.size() > 1) {
            throw new RuntimeException("중복된 경로가 검색됩니다.");
        }
        final SectionEntity originalSection = sections.get(0);
        final int nextDistance = getNewDistance(distance, originalSection);
        sectionDao.delete(originalSection);

        return List.of(insertSectionEntity(lineId, previousStationId, nextStationId, distance),
                insertSectionEntity(lineId, originalSection.getPreviousStationId(), previousStationId, nextDistance));
    }

    private static int getNewDistance(final int distance, final SectionEntity originalSection) {
        int originalSectionDistance = originalSection.getDistance();

        if (originalSectionDistance <= distance) {
            throw new IllegalArgumentException("길이 정보가 잘못되었습니다.");
        }

        return originalSectionDistance - distance;
    }

    private List<SectionEntity> saveLastStation(final long lineId, final long previousStationId,
                                                final long nextStationId, final int distance) {
        SectionEntity sectionEntity = insertSectionEntity(lineId, previousStationId, nextStationId, distance);
        return List.of(sectionEntity);
    }

    private SectionEntity insertSectionEntity(final long lineId, final long previousStationId, final long nextStationId, final int distance) {
        return sectionDao.insert(new SectionEntity.Builder()
                .lineId(lineId)
                .previousStationId(previousStationId)
                .nextStationId(nextStationId)
                .distance(distance)
                .build());
    }

}
