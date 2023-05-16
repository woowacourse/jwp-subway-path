package subway.domain.section;

import subway.domain.station.Station;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Optional<Section> findUpSectionByStation(Station station) {
        return sections.stream()
                .filter(section -> section.getDownStation() == station)
                .findAny();
    }

    public Optional<Section> findDownSectionByStation(Station station) {
        return sections.stream()
                .filter(section -> section.getUpStation() == station)
                .findAny();
    }

    public int getSectionsSize() {
        return sections.size();
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
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
