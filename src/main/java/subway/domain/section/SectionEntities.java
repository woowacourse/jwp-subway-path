package subway.domain.section;

import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SectionEntities {

    private final List<SectionEntity> sections;

    public SectionEntities(List<SectionEntity> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Optional<SectionEntity> findUpSectionByStation(StationEntity stationEntity) {
        Long stationId = stationEntity.getId();
        return sections.stream()
                .filter(section -> section.getDownStationId() == stationId)
                .findAny();
    }

    public Optional<SectionEntity> findDownSectionByStation(StationEntity stationEntity) {
        Long stationId = stationEntity.getId();
        return sections.stream()
                .filter(section -> section.getUpStationId() == stationId)
                .findAny();
    }

    public int getSize() {
        return sections.size();
    }

    public List<SectionEntity> getSections() {
        return List.copyOf(sections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionEntities sectionEntities1 = (SectionEntities) o;
        return Objects.equals(sections, sectionEntities1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
