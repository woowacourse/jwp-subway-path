package subway.domain.section;

import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

class SectionsFactory {

    static SingleLineSections createSortedSections(List<Section> sections) {
        return new SingleLineSections(sortSections(sections));
    }

    private static List<Section> sortSections(List<Section> sections) {
        final Station firstStation = getFirstStations(sections);
        final Map<Station, Section> sectionGroupByUpStation = sections.stream()
                .collect(toMap(Section::getUpStation, Function.identity()));

        List<Section> sortedSections = new ArrayList<>();
        Station next = firstStation;
        for (int i = 0; i < sections.size(); i++) {
            next = addNextSection(sectionGroupByUpStation, sortedSections, next);
        }
        return sortedSections;
    }

    private static Station getFirstStations(List<Section> sections) {
        final Set<Station> allStations = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(toSet());

        final Set<Station> downStations = sections.stream()
                .map(Section::getDownStation)
                .collect(toSet());

        final List<Station> firstStation = new ArrayList<>(allStations);
        firstStation.removeAll(downStations);

        return firstStation.get(0);
    }

    private static Station addNextSection(Map<Station, Section> sectionGroupByUpStation, List<Section> result, Station upStation) {
        final Section section = sectionGroupByUpStation.get(upStation);
        result.add(section);
        return section.getDownStation();
    }
}
