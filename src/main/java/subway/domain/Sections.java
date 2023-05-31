package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sections {
    private final Map<Station, Section> sections;

    public Sections(final Map<Station, Section> sections) {
        this.sections = sections;
    }

    public void insertSectionBetween(final Long lineId, final Station upper, final Station lower, final Distance distance) {
        Section section = new Section(lineId, upper, lower, distance);
        sections.put(upper, section);
    }

    public void deleteSection(final Station station) {
        sections.remove(station);
    }

    public Distance getDistanceBetween(final Station from, final Station to) {
        if (sections.get(from) != null && sections.get(from).isNext(to)) {
            return sections.get(from).getDistance();
        }
        return calculateDistance(from, to);
    }

    private Distance calculateDistance(final Station from, final Station to) {
        Distance distance = sections.get(from).getDistance();
        Station station = getLower(from);

        while (!station.equals(to)) {
            distance = distance.plus(sections.get(station).getDistance());
            station = getLower(station);
        }

        return distance;
    }

    private Station getLower(final Station from) {
        return sections.get(from).getLower();
    }

    public void clear() {
        sections.clear();
    }

    public List<Section> getOrderedSections() {
        Station index = findStartStation();
        List<Section> orderedSections = new ArrayList<>();

        while (sections.get(index) != null) {
            orderedSections.add(sections.get(index));
            index = getLower(index);
        }
        return orderedSections;
    }

    public List<Station> getOrderedStations() {
        Station index = findStartStation();
        List<Station> orderedStations = new ArrayList<>();

        while (sections.get(index) != null) {
            orderedStations.add(index);
            index = getLower(index);
        }
        orderedStations.add(index);
        return orderedStations;
    }

    private Station findStartStation() {
        List<Station> onlyLowerStations = sections.values()
                .stream()
                .map(Section::getLower)
                .collect(Collectors.toList());

        return sections.keySet()
                .stream()
                .filter(station -> !onlyLowerStations.contains(station))
                .findFirst()
                .orElseThrow();
    }
}
