package subway.dao;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import subway.dao.entity.SectionEntity;
import subway.domain.section.Section;

public class InMemorySectionDao implements SectionDao {
    private final Map<Long, SectionEntity> sections = new HashMap<>();
    private long sectionId = 1;

    @Override
    public long insert(Long lineId, Section section) {
        sections.put(sectionId, new SectionEntity(sectionId, lineId, section.getUpBoundStation(), section.getDownBoundStation(),
                section.getDistance()));
        return sectionId++;
    }

    @Override
    public void update(Long sectionId, Section section) {
        SectionEntity sectionEntity = sections.get(sectionId);
        if (sectionEntity != null) {
            sections.replace(sectionId,
                    new SectionEntity(sectionEntity.getId(), sectionEntity.getLineId(), section.getUpBoundStation(),
                            section.getDownBoundStation(), section.getDistance()));
        }
    }

    @Override
    public Long countByLineId(Long lineId) {
        return sections.values().stream()
                .filter(sectionEntity -> lineId.equals(sectionEntity.getLineId()))
                .count();
    }

    @Override
    public List<SectionEntity> findAllByLineId(Long lineId) {
        return sections.values().stream()
                .filter(sectionEntity -> lineId.equals(sectionEntity.getLineId()))
                .collect(toList());
    }

    @Override
    public boolean isStationInLine(Long lineId, String stationName) {
        return sections.values().stream()
                .filter(sectionEntity -> lineId.equals(sectionEntity.getLineId()))
                .anyMatch(sectionEntity -> stationName.equals(sectionEntity.getUpBoundStation().getName())
                        || stationName.equals(sectionEntity.getDownBoundStation().getName()));
    }

    @Override
    public boolean isEmptyByLineId(Long lineId) {
        return sections.values().stream()
                .noneMatch(sectionEntity -> lineId.equals(sectionEntity.getLineId()));
    }

    @Override
    public Optional<SectionEntity> findByStartStationNameAndLineId(String upBoundStationName, Long lineId) {
        return sections.values().stream()
                .filter(sectionEntity -> lineId.equals(sectionEntity.getLineId()))
                .filter(sectionEntity -> upBoundStationName.equals(sectionEntity.getUpBoundStation().getName()))
                .findAny();
    }

    @Override
    public Optional<SectionEntity> findByEndStationNameAndLineId(String downBoundStationName, Long lineId) {
        return sections.values().stream()
                .filter(sectionEntity -> lineId.equals(sectionEntity.getLineId()))
                .filter(sectionEntity -> downBoundStationName.equals(sectionEntity.getDownBoundStation().getName()))
                .findAny();
    }

    @Override
    public void deleteBy(SectionEntity sectionEntity) {
        sections.remove(sectionEntity.getId());
    }

    @Override
    public List<SectionEntity> findAll() {
        return new ArrayList<>(sections.values());
    }

    @Override
    public boolean doesNotExistByStationName(String stationName) {
        return sections.values().stream()
                .noneMatch(sectionEntity -> stationName.equals(sectionEntity.getUpBoundStation().getName())
                        || stationName.equals(sectionEntity.getDownBoundStation().getName()));
    }
}
