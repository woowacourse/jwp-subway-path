package subway.domain.section;

import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> findSectionsContainStation(Station containedStation) {
        return sections.stream()
                .filter(section -> section.isContainStation(containedStation))
                .collect(toList());
    }

    public Optional<Section> findSectionContainSection(Section containedSection) {
        return sections.stream()
                .filter(section -> section.isContainSection(containedSection))
                .findAny();
    }

    public Line getLine() {
        return sections.get(0).getLine();
    }

    public int getSectionsSize() {
        return sections.size();
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
