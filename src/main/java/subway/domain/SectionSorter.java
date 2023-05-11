package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SectionSorter {

    private static final SectionSorter instance = new SectionSorter();

    private SectionSorter() {
    }

    public static SectionSorter getInstance() {
        return instance;
    }

    public List<Section> sortSections(List<Section> sections) {
        Map<Station, Section> departureToSection = createDepartureToSection(sections);
        Station currentStation = getFirstStationFromSections(departureToSection);

        List<Section> sortedSections = new ArrayList<>();

        while (!departureToSection.isEmpty()) {
            Section currentSection = departureToSection.remove(currentStation);
            sortedSections.add(currentSection);
            currentStation = currentSection.getArrival();
        }

        return sortedSections;
    }

    private Station getFirstStationFromSections(Map<Station, Section> sectionsMap) {
        Set<Station> arrivalStations = sectionsMap.values().stream()
                .map(Section::getArrival)
                .collect(Collectors.toSet());

        return sectionsMap.keySet().stream()
                .filter(departureStation -> !arrivalStations.contains(departureStation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 노선에 시작역이 존재하지 않습니다."));
    }

    private Map<Station, Section> createDepartureToSection(List<Section> sections) {
        return sections.stream()
                .collect(Collectors.toMap(Section::getDeparture, Function.identity()));
    }

}
