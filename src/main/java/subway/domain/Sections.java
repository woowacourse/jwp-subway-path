package subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Station> getDistinctStations() {
        Set<Station> distinctStations = new HashSet<>();
        for (Section section : sections) {
            distinctStations.add(section.getUpStation());
            distinctStations.add(section.getDownStation());
        }
        return new ArrayList<>(distinctStations);
    }

    public int getTotalDistance() {
        return sections.stream()
            .mapToInt(Section::getDistance)
            .sum();
    }

    public List<Section> getSections() {
        return sections;
    }
}
