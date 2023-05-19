package subway.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.Section;

@Repository
public class SectionRepository {

    private static final HashMap<Long, Section> store = new HashMap<>();

    private final SectionDao sectionDao;

    public SectionRepository(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public Section insert(Section section) {
        Section storedSection = sectionDao.insert(section);
        store.put(storedSection.getId(), storedSection);
        return storedSection;
    }

    private void init() {
        if (store.isEmpty()) {
            List<Section> sections = sectionDao.findAll();
            for (Section section : sections) {
                store.put(section.getId(), section);
            }
        }
    }

    public List<Section> findSectionByLineIdAndStationId(final Long lineId, final Long stationId) {
        init();
        return store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .filter(section -> section.getUpStationId() == stationId || section.getDownStationId() == stationId)
                .collect(Collectors.toUnmodifiableList());
    }

    public int countByLineId(final Long lineId) {
        init();
        return (int) store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .count();
    }

    public void update(Section section) {
        sectionDao.update(section);
        store.put(section.getId(), section);
    }

    public void deleteById(Long id) {
        sectionDao.deleteById(id);
        store.remove(id);
    }

    public void deleteAllByLineId(final Long lineId) {
        sectionDao.deleteAllByLineId(lineId);
        Set<Long> ids = store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .map(Section::getId)
                .collect(Collectors.toSet());
        for (Long id : ids) {
            store.remove(id);
        }
    }

    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        Set<Long> ids = store.values().stream()
                .filter(section -> section.getLineId() == lineId)
                .filter(section -> section.getUpStationId() == stationId || section.getDownStationId() == stationId)
                .map(Section::getId)
                .collect(Collectors.toSet());
        for (Long id : ids) {
            sectionDao.deleteById(id);
        }
        for (Long id : ids) {
            store.remove(id);
        }
    }
}
