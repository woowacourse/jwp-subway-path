package subway.infrastructure.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.infrastructure.persistence.dao.LineDao;
import subway.infrastructure.persistence.dao.SectionDao;
import subway.infrastructure.persistence.dao.StationDao;
import subway.infrastructure.persistence.entity.LineEntity;
import subway.infrastructure.persistence.entity.SectionEntity;
import subway.infrastructure.persistence.entity.StationEntity;

@Repository
public class JdbcLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public JdbcLineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public Long save(final Line line) {
        final Long lineId = lineDao.save(LineEntity.from(line));
        final List<SectionEntity> entities = line.sections().stream()
                .map(it -> SectionEntity.of(it, lineId))
                .collect(Collectors.toList());
        sectionDao.batchSave(entities);
        return lineId;
    }

    @Override
    public void update(final Line line) {
        final List<SectionEntity> entities = line.sections().stream()
                .map(it -> SectionEntity.of(it, line.id()))
                .collect(Collectors.toList());
        sectionDao.deleteAllByLineName(line.name());
        sectionDao.batchSave(entities);
    }

    @Override
    public void delete(final Line line) {
        sectionDao.deleteAllByLineName(line.name());
        lineDao.delete(LineEntity.from(line));
    }

    @Override
    public Optional<Line> findById(final Long id) {
        return lineDao.findById(id)
                .map(it -> it.toDomain(getSections(it.name())));
    }

    @Override
    public Optional<Line> findByName(final String name) {
        return lineDao.findByName(name)
                .map(it -> it.toDomain(getSections(name)));
    }

    private List<Section> getSections(final String name) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineName(name);
        final Set<Long> stationIds = getStationIds(sectionEntities);
        final Map<Long, StationEntity> idStationEntityMap = stationDao.findAllByIds(stationIds)
                .stream()
                .collect(Collectors.toMap(StationEntity::id, it -> it));
        final List<Section> sections = new ArrayList<>();
        for (final SectionEntity sectionEntity : sectionEntities) {
            final Section section = mapToSection(idStationEntityMap, sectionEntity);
            sections.add(section);
        }
        return sections;
    }

    private Set<Long> getStationIds(final List<SectionEntity> sectionEntities) {
        final Set<Long> stationIds = sectionEntities.stream()
                .map(SectionEntity::downStationId)
                .collect(Collectors.toSet());
        stationIds.add(sectionEntities.get(0).upStationId());
        return stationIds;
    }

    private Section mapToSection(final Map<Long, StationEntity> idStationEntityMap,
                                 final SectionEntity sectionEntity) {
        final Station up = idStationEntityMap.get(sectionEntity.upStationId()).toDomain();
        final Station down = idStationEntityMap.get(sectionEntity.downStationId()).toDomain();
        final int distance = sectionEntity.distance();
        return new Section(up, down, distance);
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(it -> it.toDomain(getSections(it.name())))
                .collect(Collectors.toList());
    }
}
