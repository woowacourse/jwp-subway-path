package subway.domain;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections() {
        sections = List.of();
    }

    private Sections(final List<Section> sections) {
        this.sections = sections;
    }

    public boolean isHeadStation(final Station station) {
        return sections.isEmpty() || sections.get(0).getPrevStation().equals(station);
    }

    public boolean isTailStation(final Station station) {
        return sections.get(sections.size() - 1).getNextStation().equals(station);
    }

    public Sections addHead(final Section section) {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(0, section);
        return new Sections(newSections);
    }

    public Sections addTail(final Section section) {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.add(sections.size(), section);
        return new Sections(newSections);
    }

    public Sections addCentral(final Section section) {
        final LinkedList<Section> newSections = new LinkedList<>(sections);

        final Section originSection = findOriginSection(section, newSections);

        final int originIndex = newSections.indexOf(originSection);
        newSections.remove(originSection);
        newSections.add(originIndex, section);
        newSections.add(originIndex + 1,
                new Section(
                        section.getNextStation(),
                        originSection.getNextStation(),
                        originSection.getDistance().minusValue(section.getDistance())
                )
        );

        return new Sections(newSections);
    }

    private static Section findOriginSection(final Section section, final LinkedList<Section> newSections) {
        return newSections.stream()
                .filter(element -> element.getPrevStation().equals(section.getPrevStation()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("이전 역을 찾을 수 없습니다."));
    }

    public Sections removeHead() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(0);
        return new Sections(newSections);
    }

    public Sections removeTail() {
        final List<Section> newSections = new LinkedList<>(sections);
        newSections.remove(sections.size() - 1);
        return new Sections(newSections);
    }

    public Sections removeCentral(final Station station) {
        final List<Section> newSections = new LinkedList<>(sections);

        final Section beforeSection = findBeforeSection(station, newSections);
        final Section nextSection = findNextSection(station, newSections);

        final int index = newSections.indexOf(beforeSection);
        newSections.remove(beforeSection);
        newSections.remove(nextSection);
        newSections.add(index, new Section(
                beforeSection.getPrevStation(),
                nextSection.getNextStation(),
                beforeSection.getDistance().plusValue(nextSection.getDistance())
        ));
        return new Sections(newSections);
    }

    private static Section findBeforeSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.isEqualNextStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    private static Section findNextSection(final Station station, final List<Section> newSections) {
        return newSections.stream()
                .filter(section -> section.isEqualPrevStation(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 역을 찾을 수 없습니다."));
    }

    public Sections getDifferenceOfSet(final Sections otherSections) {
        final List<Section> result = new LinkedList<>(sections);
        result.removeAll(otherSections.getSections());
        return new Sections(result);
    }

    public boolean containSection(final Section otherSection) {
        final List<Station> allStations = getAllStations();
        return allStations.contains(otherSection.getPrevStation())
                && allStations.contains(otherSection.getNextStation());
    }

    public List<Station> getAllStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        final List<Station> stations = new LinkedList<>();
        for (final Section section : sections) {
            stations.add(section.getPrevStation());
        }
        stations.add(sections.get(sections.size() - 1).getNextStation());
        return stations;
    }

    public List<Section> getSections() {
        return sections;
    }
}
