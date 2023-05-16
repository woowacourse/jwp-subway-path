package subway.line.infrastructure.persistence.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;
import subway.line.domain.Station;
import subway.line.infrastructure.persistence.dao.LineDao;
import subway.line.infrastructure.persistence.dao.SectionDao;
import subway.line.infrastructure.persistence.dao.StationDao;
import subway.line.infrastructure.persistence.entity.LineEntity;
import subway.line.infrastructure.persistence.entity.SectionEntity;
import subway.line.infrastructure.persistence.entity.StationEntity;

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
    public void save(final Line line) {
        final LineEntity entity = LineEntity.from(line);
        lineDao.save(LineEntity.from(line));
        final List<SectionEntity> entities = line.sections().stream()
                .map(it -> SectionEntity.of(it, entity.domainId()))
                .collect(Collectors.toList());
        sectionDao.batchSave(entities);
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
    public Optional<Line> findById(final UUID id) {
        return lineDao.findById(id)
                .map(it -> it.toDomain(getSections(it.name())));
    }

    private List<Section> getSections(final String name) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineName(name);
        if (sectionEntities.isEmpty()) {
            return Collections.emptyList();
        }
        final Set<UUID> stationIds = getStationIds(sectionEntities);
        final Map<UUID, StationEntity> idStationEntityMap = stationDao.findAllByDomainIds(stationIds)
                .stream()
                .collect(Collectors.toMap(StationEntity::domainId, it -> it));
        final List<Section> sections = new ArrayList<>();
        for (final SectionEntity sectionEntity : sectionEntities) {
            final Section section = mapToSection(idStationEntityMap, sectionEntity);
            sections.add(section);
        }
        return sections;
    }

    private Set<UUID> getStationIds(final List<SectionEntity> sectionEntities) {
        final Set<UUID> stationIds = sectionEntities.stream()
                .map(SectionEntity::downStationDomainId)
                .collect(Collectors.toSet());
        stationIds.add(sectionEntities.get(0).upStationDomainId());
        return stationIds;
    }

    private Section mapToSection(final Map<UUID, StationEntity> idStationEntityMap,
                                 final SectionEntity sectionEntity) {
        final Station up = idStationEntityMap.get(sectionEntity.upStationDomainId()).toDomain();
        final Station down = idStationEntityMap.get(sectionEntity.downStationDomainId()).toDomain();
        final int distance = sectionEntity.distance();
        return new Section(up, down, distance);
    }

    @Override
    public Optional<Line> findByName(final String name) {
        return lineDao.findByName(name)
                .map(it -> it.toDomain(getSections(name)));
    }

    @Override
    public List<Line> findAll() {
        return lineDao.findAll()
                .stream()
                .map(it -> it.toDomain(getSections(it.name())))
                .collect(Collectors.toList());
    }
}
