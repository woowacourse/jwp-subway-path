package subway.domain;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sortSections(sections);
    }

    private List<Section> sortSections(List<Section> sections) {
        Map<Station, Section> stationToSection = sections.stream()
                .collect(toMap(Section::getStartStation, section -> section));
        Station firstStation = findFirstStation(sections);
        return getSortedSections(stationToSection, firstStation);
    }

    private Station findFirstStation(List<Section> sections) {
        Map<Station, Station> stationToStation = sections.stream()
                .collect(toMap(Section::getStartStation, Section::getEndStation));
        Set<Station> startStations = new HashSet<>(stationToStation.keySet());
        startStations.removeAll(stationToStation.values());
        return startStations.stream().findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상행 종점 역을 찾을 수 없습니다."));
    }

    private List<Section> getSortedSections(Map<Station, Section> stationToSection, Station firstStation) {
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
}
