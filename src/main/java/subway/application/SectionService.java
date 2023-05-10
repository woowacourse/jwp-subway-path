package subway.application;

import org.springframework.stereotype.Service;
import subway.application.domain.Station;
import subway.application.dto.SectionInsertDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.ui.query_option.SubwayDirection;

import java.util.List;

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
        final Long lineId = lineDao.findIdByName(sectionInsertDto.getLineName());
        final StationEntity standardStation = stationDao.findByName(sectionInsertDto.getStandardStationName());
//        final Station stadardStation = new Station(standardStation, )
        final StationEntity additionStation = stationDao.findByName(sectionInsertDto.getAdditionalStationName());
        if (sectionInsertDto.getDirection() == SubwayDirection.UP) {
            List<SectionEntity> sectionEntities = saveUpperSection(lineId, standardStation.getId(), additionStation.getId(), sectionInsertDto.getDistance());

        }



        return 1L;
    }

    private List<SectionEntity> saveUpperSection(final Long lineId, final Long previousStationId,
                                                 final Long additionalStationId, final int distance) {
        final List<SectionEntity> sections = sectionDao.findByLineIdAndPreviousStationId(lineId, previousStationId);

        if (sections.isEmpty()) {
            return saveLastStation(lineId, previousStationId, additionalStationId, distance);
        }

        return saveBetweenStation(sections, lineId, previousStationId, additionalStationId, distance);
    }

    private List<SectionEntity> saveDownSection(final Long lineId, final Long nextStationId,
                                                 final Long additionalStationId, final int distance) {
        final List<SectionEntity> sections = sectionDao.findByLineIdAndPreviousStationId(lineId, nextStationId);

        if (sections.isEmpty()) {
            return saveLastStation(lineId, additionalStationId, nextStationId, distance);
        }

        return saveBetweenStation(sections, lineId, additionalStationId, nextStationId, distance);
    }

    private List<SectionEntity> saveBetweenStation(final List<SectionEntity> sections,
                                                   final Long lineId,
                                                   final Long previousStationId,
                                                   final Long additionalStationId,
                                                   final int distance) {
        if (sections.size() > 1) {
            throw new RuntimeException("중복된 경로가 검색됩니다.");
        }
        final SectionEntity originalSection = sections.get(0);
        final int nextDistance = getNewDistance(distance, originalSection);
        sectionDao.delete(originalSection);
        final SectionEntity newPreviousSection = getSectionEntity(lineId, previousStationId, additionalStationId, distance);
        final SectionEntity newNextSection = getSectionEntity(lineId, additionalStationId, originalSection.getNextStationId(), nextDistance);

        return List.of(sectionDao.insert(newPreviousSection),
                sectionDao.insert(newNextSection));
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
        SectionEntity sectionEntity = getSectionEntity(lineId, previousStationId, nextStationId, distance);
        return List.of(sectionEntity);
    }

    private SectionEntity getSectionEntity(final long lineId, final long previousStationId, final long nextStationId, final int distance) {
        SectionEntity sectionEntity = sectionDao.insert(new SectionEntity.Builder()
                .lineId(lineId).previousStationId(previousStationId)
                .nextStationId(nextStationId).distance(distance)
                .build());
        return sectionEntity;
    }

}
