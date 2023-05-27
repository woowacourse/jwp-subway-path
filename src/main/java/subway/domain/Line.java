package subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final Sections sections;

    public Line(final String name) {
        this.id = null;
        this.name = name;
        this.sections = new Sections(new LinkedList<>());
    }

    public Line(final Long id, final String name, final LinkedList<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = new Sections(sections);
    }

    public void addSection(final Section newSection) {
        Station leftStation = newSection.getLeft();
        Station rightStation = newSection.getRight();
        newSection.validateSameStation(leftStation, rightStation);

        if (sections.isEmpty()) {
            sections.add(newSection);
            return;
        }
        if (sections.hasLeftStationInSections(leftStation)) {
            addStationAtRight(newSection, leftStation, rightStation);
            return;
        }
        if (sections.hasRightStationInSections(rightStation)) {
            addStationAtLeft(newSection, leftStation, rightStation);
            return;
        }
        addSectionAtEnd(newSection, leftStation, rightStation);
    }

    private void addStationAtRight(
            final Section newSection,
            final Station leftStation,
            final Station rightStation
    ) {
        Section foundSection = sections.findSectionByLeftStation(leftStation);
        int indexOfFoundSection = sections.indexOf(foundSection);
        int foundSectionDistance = foundSection.getDistance();
        int newSectionDistance = newSection.getDistance();

        validateDistance(foundSectionDistance, newSectionDistance);
        Section dividedSection = new Section(rightStation, foundSection.getRight(),
                foundSectionDistance - newSectionDistance);
        sections.remove(foundSection);
        sections.add(indexOfFoundSection, newSection);
        sections.add(indexOfFoundSection + 1, dividedSection);
    }

    private void addStationAtLeft(Section newSection, Station leftStation, Station rightStation) {
        Section foundSection = sections.findSectionByRightStation(rightStation);
        int indexOfFoundSection = sections.indexOf(foundSection);
        int foundSectionDistance = foundSection.getDistance();
        int newSectionDistance = newSection.getDistance();

        validateDistance(foundSectionDistance, newSectionDistance);
        Section dividedSection = new Section(foundSection.getLeft(), leftStation,
                foundSectionDistance - newSectionDistance);
        sections.remove(foundSection);
        sections.add(indexOfFoundSection, newSection);
        sections.add(indexOfFoundSection, dividedSection);
    }

    private void addSectionAtEnd(
            final Section newSection,
            final Station leftStation,
            final Station rightStation
    ) {
        if (sections.hasLeftStationInSections(rightStation) && !sections.hasRightStationInSections(rightStation)) {
            sections.addFirst(newSection);
        }
        if (sections.hasRightStationInSections(leftStation) && !sections.hasLeftStationInSections(leftStation)) {
            sections.addLast(newSection);
        }
    }

    private void validateDistance(final int foundSectionDistance, final int newSectionDistance) {
        if (foundSectionDistance <= newSectionDistance) {
            throw new IllegalArgumentException("삽입되는 구간의 길이는 원래 구간의 길이를 넘을 수 없습니다.");
        }
    }

    public void deleteSection(final Station station) {
        validateStationInLine(station);

        if (sections.size() == 1) {
            sections.remove();
            return;
        }
        if (sections.hasLeftStationInSections(station) && sections.hasRightStationInSections(station)) {
            deleteSectionAtMiddle(station);
            return;
        }
        deleteSectionAtEnd(station);
    }

    private void validateStationInLine(final Station station) {
        if (!sections.hasStationInSections(station)) {
            throw new IllegalArgumentException("노선에 해당 역이 존재하지 않습니다.");
        }
    }

    private void deleteSectionAtMiddle(final Station station) {
        Section leftSection = sections.findSectionByRightStation(station);
        Section rightSection = sections.findSectionByLeftStation(station);
        int indexToAdd = sections.indexOf(leftSection);

        sections.remove(leftSection);
        sections.remove(rightSection);
        Section newSection = new Section(leftSection.getLeft(), rightSection.getRight(),
                leftSection.getDistance() + rightSection.getDistance());
        sections.add(indexToAdd, newSection);
    }

    private void deleteSectionAtEnd(final Station station) {
        if (isLastStationAtLeft(station)) {
            sections.removeFirst();
        }
        if (isLastStationAtRight(station)) {
            sections.removeLast();
        }
    }

    public boolean isLastStationAtLeft(final Station station) {
        List<Station> leftStations = sections.findLeftStations();
        List<Station> rightStations = sections.findRightStations();
        leftStations.removeAll(rightStations);
        return leftStations.stream()
                .anyMatch(leftStation -> leftStation.equals(station));
    }

    public boolean isLastStationAtRight(final Station station) {
        List<Station> leftStations = sections.findLeftStations();
        List<Station> rightStations = sections.findRightStations();
        rightStations.removeAll(leftStations);
        return rightStations.stream()
                .anyMatch(rightStation -> rightStation.equals(station));
    }

    public List<Station> findLeftToRightRoute() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        stations.add(0, sections.getFirst().getLeft());
        for (final Section section : sections.getSections()) {
            stations.add(section.getRight());
        }
        return stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Sections getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Line line = (Line) o;
        return Objects.equals(getId(), line.getId()) && Objects.equals(getName(), line.getName())
                && Objects.equals(getSections(), line.getSections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSections());
    }
}
