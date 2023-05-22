package subway.domain;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSortedSections() {
        Map<Station, Section> stationToSection = sections.stream()
            .collect(toMap(Section::getStartStation, section -> section));
        return findFirstStation(sections)
            .map(firstStation -> sortByFirstStation(stationToSection, firstStation))
            .orElse(Collections.emptyList());
    }

    private Optional<Station> findFirstStation(List<Section> sections) {
        Map<Station, Station> stationToStation = sections.stream()
            .collect(toMap(Section::getStartStation, Section::getEndStation));
        Set<Station> startStations = new HashSet<>(stationToStation.keySet());
        startStations.removeAll(stationToStation.values());
        return startStations.stream().findFirst();
    }

    private List<Section> sortByFirstStation(Map<Station, Section> stationToSection,
        Station firstStation) {
        List<Section> sortedSection = new ArrayList<>();
        Section section = stationToSection.get(firstStation);
        sortedSection.add(section);
        while (stationToSection.size() != sortedSection.size()) {
            section = stationToSection.get(section.getEndStation());
            sortedSection.add(section);
        }
        return sortedSection;
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    public boolean containsStation(Station station) {
        return sections.stream()
            .anyMatch(section -> section.hasStation(station));
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Optional<Section> findSectionWithEndStation(Station station) {
        return sections.stream()
            .filter(section -> section.hasStationInEndPosition(station))
            .findAny();
    }

    public Optional<Section> findSectionWithStartStation(Station station) {
        return sections.stream()
            .filter(section -> section.hasStationInStartPosition(station))
            .findAny();
    }
}
