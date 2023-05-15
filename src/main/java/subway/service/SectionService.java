package subway.service;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import subway.service.domain.Section;
import subway.service.dto.SectionInsertDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.controller.dto.response.SectionResponse;
import subway.service.domain.SubwayDirection;

import java.util.List;

import static subway.service.converter.LineConverter.entityToDomain;
import static subway.service.converter.SectionConverter.domainToResponseDtos;
import static subway.service.converter.StationConverter.entityToDomain;

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

    public List<SectionResponse> save(final SectionInsertDto sectionInsertDto) {
        final LineEntity line = lineDao.findByName(sectionInsertDto.getLineName());
        final StationEntity standardStation = stationDao.findByName(sectionInsertDto.getStandardStationName());
        final StationEntity additionStation = stationDao.findByName(sectionInsertDto.getAdditionalStationName());

        if (sectionInsertDto.getDirection() == SubwayDirection.UP) {
            return domainToResponseDtos(saveUpperSection(line, standardStation, additionStation, sectionInsertDto.getDistance()));
        }

        return domainToResponseDtos(saveDownSection(line, standardStation, additionStation, sectionInsertDto.getDistance()));
    }

    private List<Section> saveUpperSection(final LineEntity line,
                                                 final StationEntity previousStation,
                                                 final StationEntity additionalStation,
                                                 final int distance) {
        final List<SectionEntity> sections = sectionDao.findByLineIdAndPreviousStationId(line.getId(), previousStation.getId());

        if (sections.isEmpty()) {
            return saveLastStation(line, previousStation, additionalStation, distance);
        }

        return saveBetweenStationWhenUpper(sections, line, previousStation, additionalStation, distance);
    }

    private List<Section> saveDownSection(final LineEntity line,
                                                final StationEntity nextStation,
                                                final StationEntity additionalStation,
                                                final int distance) {
        final List<SectionEntity> sections = sectionDao.findByLineIdAndNextStationId(line.getId(), nextStation.getId());

        if (sections.isEmpty()) {
            return saveLastStation(line, additionalStation, nextStation, distance);
        }

        return saveBetweenStationWhenDown(sections, line, additionalStation, nextStation, distance);
    }

    private List<Section> saveBetweenStationWhenUpper(final List<SectionEntity> sections,
                                                   final LineEntity line,
                                                   final StationEntity previousStation,
                                                   final StationEntity nextStation,
                                                   final int distance) {
        if (sections.size() > 1) {
            throw new RuntimeException("중복된 경로가 검색됩니다.");
        }

        final SectionEntity originalSection = sections.get(0);
        final int nextDistance = getNewDistance(distance, originalSection);
        sectionDao.delete(originalSection);
        SectionEntity backSectionEntity = insertSectionEntity(line.getId(), previousStation.getId(), nextStation.getId(), distance);
        SectionEntity frontSectionEntity = insertSectionEntity(line.getId(), nextStation.getId(), originalSection.getNextStationId(), nextDistance);

        return List.of(sectionEntityToSection(backSectionEntity, line, previousStation, nextStation, distance),
                sectionEntityToSection(frontSectionEntity, line, nextStation, stationDao.findById(originalSection.getNextStationId()), nextDistance));
    }

    private List<Section> saveBetweenStationWhenDown(final List<SectionEntity> sections,
                                                            final LineEntity line,
                                                            final StationEntity previousStation,
                                                            final StationEntity nextStation,
                                                            final int distance) {
        if (sections.size() > 1) {
            throw new RuntimeException("중복된 경로가 검색됩니다.");
        }

        final SectionEntity originalSection = sections.get(0);
        final int nextDistance = getNewDistance(distance, originalSection);
        sectionDao.delete(originalSection);
        SectionEntity frontSectionEntity = insertSectionEntity(line.getId(), previousStation.getId(), nextStation.getId(), distance);
        SectionEntity backSectionEntity = insertSectionEntity(line.getId(), originalSection.getPreviousStationId(), previousStation.getId(), nextDistance);

        return List.of(sectionEntityToSection(frontSectionEntity, line, previousStation, nextStation, distance),
                sectionEntityToSection(backSectionEntity, line, stationDao.findById(originalSection.getPreviousStationId()), previousStation, nextDistance));
    }

    private int getNewDistance(final int distance, final SectionEntity originalSection) {
        int originalSectionDistance = originalSection.getDistance();

        if (originalSectionDistance <= distance) {
            throw new IllegalArgumentException("길이 정보가 잘못되었습니다.");
        }

        return originalSectionDistance - distance;
    }

    private List<Section> saveLastStation(final LineEntity lineEntity,
                                                final StationEntity previousStationEntity,
                                                final StationEntity nextStationEntity,
                                                final int distance) {
        SectionEntity sectionEntity = insertSectionEntity(lineEntity.getId(), previousStationEntity.getId(), nextStationEntity.getId(), distance);

        return List.of(sectionEntityToSection(sectionEntity, lineEntity, previousStationEntity, nextStationEntity, distance));
    }

    private SectionEntity insertSectionEntity(final long lineId, final long previousStationId, final long nextStationId, final int distance) {
        return sectionDao.insert(new SectionEntity.Builder()
                .lineId(lineId)
                .previousStationId(previousStationId)
                .nextStationId(nextStationId)
                .distance(distance)
                .build());
    }

    private Section sectionEntityToSection(final SectionEntity sectionEntity, final LineEntity lineEntity, StationEntity previousStationEntity, StationEntity nextStationEntity, final int distance) {
        return new Section(
                sectionEntity.getId(),
                entityToDomain(lineEntity),
                entityToDomain(previousStationEntity),
                entityToDomain(nextStationEntity),
                distance
        );
    }

    public void remove(final Long lineId, final Long stationId) {
        List<SectionEntity> originalSections = sectionDao.findByLineIdAndPreviousStationIdOrNextStationId(lineId, stationId);

        if (originalSections.size() == 1) {
            sectionDao.delete(originalSections.get(0));
            return;
        }

        removeStationInLineWhenTwoSection(lineId, stationId, originalSections);
    }

    private void removeStationInLineWhenTwoSection(final Long lineId, final Long stationId, final List<SectionEntity> originalSections) {
        SectionEntity nextSection = originalSections.stream()
                .filter(section -> section.getPreviousStationId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new DataAccessResourceFailureException("데이터 정보가 잘못되었습니다."));
        SectionEntity previousSection = originalSections.stream()
                .filter(section -> section.getNextStationId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new DataAccessResourceFailureException("데이터 정보가 잘못되었습니다."));

        int distance = nextSection.getDistance() + previousSection.getDistance();
        insertSectionEntity(lineId, previousSection.getPreviousStationId(), nextSection.getNextStationId(), distance);
        originalSections.forEach(sectionDao::delete);
    }
}
