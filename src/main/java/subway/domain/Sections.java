package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    private final LinkedList<Section> sections;

    public Sections(final LinkedList<Section> sections) {
        this.sections = sections;
    }

    public boolean hasLeftStationInSections(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.getLeft().equals(station));
    }

    public boolean hasRightStationInSections(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.getRight().equals(station));
    }

    public Section findSectionByLeftStation(final Station station) {
        return sections.stream()
                .filter(section -> section.getLeft().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }

    public Section findSectionByRightStation(final Station station) {
        return sections.stream()
                .filter(section -> section.getRight().equals(station))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }

    public List<Station> findLeftStations() {
        return sections.stream()
                .map(Section::getLeft)
                .collect(Collectors.toList());
    }

    public List<Station> findRightStations() {
        return sections.stream()
                .map(Section::getRight)
                .collect(Collectors.toList());
    }

    public boolean hasStationInSections(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.hasStation(station));
    }

    public void add(final Section section) {
        sections.add(section);
    }

    public void add(final int index, final Section section) {
        sections.add(index, section);
    }

    public void addFirst(final Section section) {
        sections.addFirst(section);
    }

    public void addLast(final Section section) {
        sections.addLast(section);
    }

    public void remove() {
        sections.remove();
    }

    public void removeFirst() {
        sections.removeFirst();
    }

    public void removeLast() {
        sections.removeLast();
    }

    public void remove(final Section section) {
        sections.remove(section);
    }

    public int size() {
        return sections.size();
    }

    public int indexOf(final Section section) {
        return sections.indexOf(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Section getFirst() {
        return sections.getFirst();
    }

    public LinkedList<Section> getSections() {
        return sections;
    }
}
