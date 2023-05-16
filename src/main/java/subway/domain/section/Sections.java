package subway.domain.section;

import subway.domain.station.Station;
import subway.exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    private final LinkedList<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new LinkedList<>(sections);
    }

    public List<Station> getStations() {
        final Map<Station, Station> stationToStation = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        final Set<Station> upStations = new HashSet<>(stationToStation.keySet());
        upStations.removeAll(stationToStation.values());

        final List<Station> result = new ArrayList<>(upStations);
        Station targetStation = result.get(0);
        while (stationToStation.containsKey(targetStation)) {
            final Station next = stationToStation.get(targetStation);
            result.add(next);
            targetStation = next;
        }
        return result;
    }

    public Sections add(final Station existStation, final Station newStation, final DirectionStrategy strategy, final Distance distance) {

        return strategy.calculate(sections, existStation, newStation, distance);
    }

    public Sections delete(final Station station) {
        final List<Section> targetSections = sections.stream()
                .filter(section -> section.hasStation(station))
                .collect(Collectors.toList());
        validateRemoveStation(station, targetSections);

        if (targetSections.size() == 1) {
            sections.remove(targetSections.get(0));
        }
        if (targetSections.size() == 2) {
            final Section section1 = targetSections.get(0);
            final Section section2 = targetSections.get(1);

            final Section newSection = new Section(section1.getUpStation(), section2.getDownStation(), section1.getDistance().plus(section2.getDistance()));
            final int targetIndex = targetSections.indexOf(section1);
            sections.remove(section1);
            sections.remove(section2);
            sections.add(targetIndex, newSection);
        }

        return new Sections(sections);
    }

    private void validateRemoveStation(final Station station, final List<Section> sections) {
        if (sections.isEmpty()) {
            throw new StationNotFoundException(station.getId());
        }
        if (sections.size() > 2) {
            throw new UnsupportedOperationException("해당 노선에 갈래길이 존재합니다. 확인해주세요.");
        }
    }

    public List<Section> getSections() {
        return new LinkedList<>(sections);
    }
}