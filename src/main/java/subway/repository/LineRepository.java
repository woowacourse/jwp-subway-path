package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long saveLine(final String name, final String color) {
        return lineDao.insert(name, color);
    }

    public Line findByLineId(final Long lineId) {
        final LineEntity lineEntity = getLineEntityOrThrowException(lineId);
        final Sections sections = collectSectionsByLineId(lineId);

        return lineEntity.toDomain(sections);
    }

    private LineEntity getLineEntityOrThrowException(final Long lineId) {
        return lineDao.findByLineId(lineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노선 식별자값은 존재하지 않는 노선의 식별자값입니다."));
    }

    private Sections collectSectionsByLineId(final Long lineId) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        final List<StationEntity> upStationEntities = collectUpStationEntities(sectionEntities);
        final List<StationEntity> downStationEntities = collectDownStationEntities(sectionEntities);
        final List<Section> sections = collectSectionsInOrder(sectionEntities, upStationEntities, downStationEntities);

        return Sections.from(sections);
    }

    private List<StationEntity> collectUpStationEntities(final List<SectionEntity> sectionEntities) {
        if (sectionEntities.isEmpty()) {
            return Collections.emptyList();
        }
        return stationDao.findInStationIds(collectUpStationIds(sectionEntities));
    }

    private List<Long> collectUpStationIds(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(SectionEntity::getUpStationId)
                .collect(Collectors.toList());
    }

    private List<StationEntity> collectDownStationEntities(final List<SectionEntity> sectionEntities) {
        if (sectionEntities.isEmpty()) {
            return Collections.emptyList();
        }
        return stationDao.findInStationIds(collectDownStationIds(sectionEntities));
    }

    private List<Long> collectDownStationIds(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(SectionEntity::getDownStationId)
                .collect(Collectors.toList());
    }

    private List<Section> collectSectionsInOrder(
            final List<SectionEntity> sectionEntities,
            final List<StationEntity> upStationEntities,
            final List<StationEntity> downStationEntities
    ) {
        final Map<Long, StationEntity> upStations = collectStationsToMap(upStationEntities);
        final Map<Long, StationEntity> downStations = collectStationsToMap(downStationEntities);

        return sectionEntities.stream()
                .map(sectionEntity -> sectionEntity.toDomain(
                        upStations.get(sectionEntity.getUpStationId()).toDomain(),
                        downStations.get(sectionEntity.getDownStationId()).toDomain()
                )).collect(Collectors.toList());
    }

    private Map<Long, StationEntity> collectStationsToMap(final List<StationEntity> stationEntities) {
        return stationEntities.stream()
                .collect(Collectors.toMap(StationEntity::getId, Function.identity()));
    }

    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> lineEntity.toDomain(collectSectionsByLineId(lineEntity.getId())))
                .collect(Collectors.toList());
    }

    public Line findByLineName(final String lineName) {
        final LineEntity lineEntity = lineDao.findByLineName(lineName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선명입니다."));
        final Sections sections = collectSectionsByLineId(lineEntity.getId());

        return lineEntity.toDomain(sections);
    }
}
