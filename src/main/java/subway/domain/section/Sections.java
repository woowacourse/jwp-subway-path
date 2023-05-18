package subway.domain.section;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {

    private static final int UNIQUE = 1;
    private static final int NEED_TO_ADJUST = 2;
    private final List<Section> sections;

    public Sections(final List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Sections insert(final Station from, final Station to, final int distance) {
        validateExistence(from, to);
        validateConnection(from, to);

        if (sections.isEmpty()) {
            sections.add(new Section(from, to, distance));
            return new Sections(sections);
        }

        if (canInsertAtEnds(from, to)) {
            sections.add(new Section(from, to, distance));
            return new Sections(sections);
        }

        changeSection(from, to, distance);
        sections.add(new Section(from, to, distance));
        return new Sections(sections);
    }

    private void validateExistence(final Station from, final Station to) {
        if (contains(from) && contains(to)) {
            throw new IllegalArgumentException("이미 호선에 존재하는 역입니다.");
        }
    }

    private void validateConnection(final Station from, final Station to) {
        if ((!contains(from) && !contains(to)) && !sections.isEmpty()) {
            throw new IllegalArgumentException("역이 기존 호선과 연결되어야 합니다.");
        }
    }

    private boolean contains(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private boolean canInsertAtEnds(final Station from, final Station to) {
        return canInsertAtRightEnd(from) || canInsertAtLeftEnd(to);
    }

    private boolean canInsertAtRightEnd(final Station station) {
        return hasUniquely(station) && sections.stream()
                .anyMatch(section -> section.containsOnRight(station));
    }

    private boolean canInsertAtLeftEnd(final Station station) {
        return hasUniquely(station) && sections.stream()
                .anyMatch(section -> section.containsOnLeft(station));
    }

    private boolean hasUniquely(final Station station) {
        return sections.stream()
                .filter(section -> section.contains(station))
                .count() == UNIQUE;
    }

    private void changeSection(final Station from, final Station to, final int distance) {
        final Section changedSection = sections.stream()
                .filter(section -> section.containsOnRight(to) && section.isInsertable(distance))
                .findFirst()
                .map(section -> {
                    sections.remove(section);
                    return section.changeRight(from, distance);
                })
                .orElseGet(() -> sections.stream()
                        .filter(section -> section.containsOnLeft(from) && section.isInsertable(distance))
                        .findFirst()
                        .map(section -> {
                            sections.remove(section);
                            return section.changeLeft(to, distance);
                        })
                        .orElseThrow(() -> new IllegalArgumentException("기존 두 역 사이의 거리가 부족합니다.")));

        sections.add(changedSection);
    }

    public Sections delete(final Station station) {
        final List<Section> targetSections = getTargetSections(station);

        if (targetSections.isEmpty()) {
            throw new IllegalArgumentException("입력한 역이 호선에 존재하지 않습니다.");
        }

        if (targetSections.size() == NEED_TO_ADJUST) {
            sections.add(new Section(
                    getLeftStation(station, targetSections),
                    getRightStation(station, targetSections),
                    sumDistance(targetSections)
            ));
        }
        sections.removeAll(targetSections);
        return new Sections(sections);
    }

    private List<Section> getTargetSections(final Station station) {
        return sections.stream()
                .filter(section -> section.contains(station))
                .collect(Collectors.toUnmodifiableList());
    }

    private Station getLeftStation(final Station station, final List<Section> sections) {
        return sections.stream()
                .filter(section -> section.containsOnRight(station))
                .findFirst()
                .map(Section::getFrom)
                .orElse(null);
    }

    private Station getRightStation(final Station station, final List<Section> sections) {
        return sections.stream()
                .filter(section -> section.containsOnLeft(station))
                .findFirst()
                .map(Section::getTo)
                .orElse(null);
    }

    private int sumDistance(final List<Section> targetSections) {
        return targetSections.stream()
                .mapToInt(Section::getDistanceValue)
                .sum();
    }

    public List<Station> getOrderedStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        final Station first = findFirstStation();
        return generateOrderedStations(first);
    }

    private Station findFirstStation() {
        Set<Station> toStations = sections.stream()
                .map(Section::getTo)
                .collect(Collectors.toSet());

        return sections.stream()
                .map(Section::getFrom)
                .filter(from -> !toStations.contains(from))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("첫번째 역을 찾을 수 없습니다."));
    }

    private List<Station> generateOrderedStations(final Station start) {
        final List<Station> orderedStations = new ArrayList<>();
        orderedStations.add(start);

        Station station = start;
        while (orderedStations.size() <= sections.size()) {
            final Station nextStation = getRightStation(station, sections);
            if (nextStation == null) {
                break;
            }
            station = nextStation;
            orderedStations.add(station);
        }
        return orderedStations;
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Sections sections1 = (Sections) o;
        return Objects.equals(sections, sections1.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }

    @Override
    public String toString() {
        return "Sections{" +
                "sections=" + sections +
                '}';
    }
}
