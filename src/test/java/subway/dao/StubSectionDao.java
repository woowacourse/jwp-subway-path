package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import subway.domain.Section;

public class StubSectionDao implements SectionDao {

    private final Map<Long, Section> sectionMap = new HashMap<>();
    private final AtomicLong maxId = new AtomicLong();

    @Override
    public Section insert(final Section section) {
        final long currentId = maxId.incrementAndGet();
        final Section saved = new Section(currentId, section);
        sectionMap.put(currentId, saved);
        return saved;
    }

    @Override
    public Optional<Section> findById(final Long id) {
        final Section section = sectionMap.get(id);
        if (section == null) {
            return Optional.empty();
        }
        return Optional.of(section);
    }

    @Override
    public void deleteById(final Long id) {
        sectionMap.remove(id);
    }

    @Override
    public List<Section> findAll() {
        return null;
    }

    @Override
    public List<Section> findByLineId(final Long lineId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Section> findUpSection(final Long lineId, final Long stationId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId) && it.getDownStation().getId().equals(stationId))
                .findFirst();
    }

    @Override
    public Optional<Section> findDownSection(final Long lineId, final Long stationId) {
        return sectionMap.values()
                .stream()
                .filter(it -> it.getLineId().equals(lineId) && it.getUpStation().getId().equals(stationId))
                .findFirst();
    }
}
