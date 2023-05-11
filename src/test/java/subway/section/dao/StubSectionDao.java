package subway.section.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import subway.section.domain.Direction;
import subway.section.domain.Section;
import subway.section.entity.SectionEntity;

public class StubSectionDao implements SectionDao {

    private final Map<Long, SectionEntity> sectionMap = new HashMap<>();
    private final AtomicLong maxId = new AtomicLong();

    @Override
    public SectionEntity insert(final SectionEntity sectionEntity) {
        final long currentId = maxId.incrementAndGet();
        final SectionEntity saved = new SectionEntity(currentId, sectionEntity);
        sectionMap.put(currentId, saved);
        return saved;
    }

    @Override
    public Optional<SectionEntity> findById(final Long id) {
        final SectionEntity sectionEntity = sectionMap.get(id);
        if (sectionEntity == null) {
            return Optional.empty();
        }
        return Optional.of(sectionEntity);
    }

    @Override
    public void deleteById(final Long id) {
        sectionMap.remove(id);
    }

    @Override
    public List<SectionEntity> findByLineId(final Long lineId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Section> findSectionsByLineId(final Long lineId) {
        return null;
    }

    @Override
    public Optional<SectionEntity> findNeighborSection(final Long lineId, final Long baseId, final Direction direction) {
        if (direction == Direction.UP) {
            return findNeighborUpSection(lineId, baseId);
        }
        return findNeighborDownSection(lineId, baseId);
    }

    public Optional<SectionEntity> findNeighborUpSection(final Long lineId, final Long stationId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId) && it.getDownStationId().equals(stationId))
                .findFirst();
    }

    public Optional<SectionEntity> findNeighborDownSection(final Long lineId, final Long stationId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId) && it.getUpStationId().equals(stationId))
                .findFirst();
    }
}
