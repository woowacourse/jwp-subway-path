package subway.domain;

import java.util.ArrayList;
import java.util.LinkedList;
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

    public List<Section> getSortedSections() {
        List<Station> notTop = sections.values()
                .stream()
                .map(Section::getLower)
                .collect(Collectors.toList());

        Station start = sections.keySet()
                .stream()
                .filter(station -> !notTop.contains(station))
                .findFirst()
                .orElseThrow();

        List<Section> sortedSections = new LinkedList<>();
        while (sections.get(start) != null) {
            sortedSections.add(sections.get(start));
            start = getLower(start);
        }

        return sortedSections;
    }

    public List<Station> getSortedStations() {
        List<Section> sortedSections = getSortedSections();
        List<Station> sortedStations = new ArrayList<>(List.of(sortedSections.get(0).getUpper()));
        for (Section section : sortedSections) {
            sortedStations.add(section.getLower());
        }

        return sortedStations;
    }
}
