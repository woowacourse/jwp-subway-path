package subway.domain.section;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static subway.domain.section.Direction.LEFT;
import static subway.domain.section.Direction.RIGHT;

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
        return canInsertAtEnd(from, RIGHT) || canInsertAtEnd(to, LEFT);
    }

    private boolean canInsertAtEnd(final Station station, final Direction direction) {
        return hasUniquely(station) && sections.stream()
                .anyMatch(section -> section.containsOn(station, direction));
    }

    private boolean hasUniquely(final Station station) {
        return sections.stream()
                .filter(section -> section.contains(station))
                .count() == UNIQUE;
    }

    private void changeSection(final Station from, final Station to, final int distance) {
        final Section changedSection = updateSectionForDistance(from, to, distance, RIGHT)
                .orElseGet(() -> updateSectionForDistance(to, from, distance, LEFT)
                        .orElseThrow(() -> new IllegalArgumentException("기존 두 역 사이의 거리가 부족합니다.")));

        sections.add(changedSection);
    }

    private Optional<Section> updateSectionForDistance(final Station from, final Station to, final int distance, final Direction direction) {
        return sections.stream()
                .filter(section -> section.containsOn(to, direction) && section.isInsertable(distance))
                .findFirst()
                .map(section -> {
                    sections.remove(section);
                    return section.change(from, distance, direction);
                });
    }

    public Sections delete(final Station station) {
        final List<Section> targetSections = getTargetSections(station);

        if (targetSections.isEmpty()) {
            throw new IllegalArgumentException("입력한 역이 호선에 존재하지 않습니다.");
        }

        if (targetSections.size() == NEED_TO_ADJUST) {
            sections.add(new Section(
                    getAdjacentStation(station, targetSections, LEFT),
                    getAdjacentStation(station, targetSections, RIGHT),
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

    private Station getAdjacentStation(final Station station, final List<Section> sections, final Direction direction) {
        return sections.stream()
                .filter(section -> section.containsOn(station, direction.reverse()))
                .findFirst()
                .map(section -> section.getStationOn(direction))
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
            final Station nextStation = getAdjacentStation(station, sections, RIGHT);
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
