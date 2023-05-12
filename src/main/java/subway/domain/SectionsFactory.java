package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

class SectionsFactory {

    static Sections createSortedSections(List<Section> sections) {
        return new Sections(sortSections(sections));
    }

    private static List<Section> sortSections(List<Section> sections) {
        final Station firstStation = getFirstStations(sections);
        List<Section> result = new ArrayList<>();
        Station next = firstStation;
        for (int i = 0; i < sections.size(); i++) {
            next = addNextSection(sections, result, next);
        }
        return result;
    }

    private static Station getFirstStations(List<Section> sections) {
        final Set<Station> allStationIds = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .collect(toSet());

        final Set<Station> downStationIds = sections.stream()
                .map(Section::getDownStation)
                .collect(toSet());

        final List<Station> firstStation = new ArrayList<>(allStationIds);
        firstStation.removeAll(downStationIds);

        return firstStation.get(0);
    }

    private static Station addNextSection(List<Section> sections, List<Section> result, Station nextStation) {
        for (Section section : sections) {
            if (section.getUpStation().equals(nextStation)) {
                nextStation = section.getDownStation();
                result.add(section);
                break;
            }
        }
        return nextStation;
    }
}
