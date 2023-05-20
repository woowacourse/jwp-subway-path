package subway.application;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import subway.domain.Section;
import subway.repository.SectionRepository;

public class SectionRepositoryFake implements SectionRepository {

    private static final HashMap<Long, Section> store = new HashMap<>();

    @Override
    public Section insert(final Section section) {
        store.put(section.getId(), section);
        return section;
    }

    @Override
    public List<Section> findAllByLineId(final Long lineId) {
        return store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<Section> findSectionByLineIdAndStationId(final Long lineId, final Long stationId) {
        return store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .filter(section -> section.getUpStationId() == stationId || section.getDownStationId() == stationId)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public int countByLineId(final Long lineId) {
        return (int) store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .count();
    }

    @Override
    public void update(final Section section) {
        store.put(section.getId(), section);
    }

    @Override
    public void deleteById(final Long id) {
        store.remove(id);
    }

    @Override
    public void deleteAllByLineId(final Long lineId) {
        Set<Long> ids = store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .map(Section::getId)
                .collect(Collectors.toSet());
        for (Long id : ids) {
            store.remove(id);
        }
    }

    @Override
    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        Set<Long> ids = store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .filter(section -> section.getUpStationId() == stationId || section.getDownStationId() == stationId)
                .map(Section::getId)
                .collect(Collectors.toSet());
        for (Long id : ids) {
            store.remove(id);
        }
    }
}
