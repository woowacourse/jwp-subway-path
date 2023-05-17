package subway.domain.section;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import subway.domain.station.Station;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sortSections(sections);
    }

    private List<Section> sortSections(List<Section> sections) {
        Map<Station, Section> stationToSection = sections.stream()
                .collect(toMap(Section::getUpBoundStation, section -> section));
        return findFirstStation(sections).map(firstStation -> getSortedSections(stationToSection, firstStation))
                .orElse(Collections.emptyList());
    }

    private Optional<Station> findFirstStation(List<Section> sections) {
        Map<Station, Station> upBoundToDownBound = sections.stream()
                .collect(toMap(Section::getUpBoundStation, Section::getDownBoundStation));
        Set<Station> startStations = new HashSet<>(upBoundToDownBound.keySet());
        startStations.removeAll(upBoundToDownBound.values());
        return startStations.stream().findFirst();
    }

    private List<Section> getSortedSections(Map<Station, Section> stationToSection, Station firstStation) {
        List<Section> sortedSection = new ArrayList<>();
        Section section = stationToSection.get(firstStation);
        sortedSection.add(section);
        while (stationToSection.size() != sortedSection.size()) {
            section = stationToSection.get(section.getDownBoundStation());
            sortedSection.add(section);
        }
        return sortedSection;
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }
}
