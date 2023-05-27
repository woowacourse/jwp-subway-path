package subway.domain;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static subway.domain.Direction.LEFT;
import static subway.domain.Direction.RIGHT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import subway.exception.InvalidSectionException;
import subway.exception.LineNotEmptyException;
import subway.exception.StationNotFoundException;

public class Line {

    private static final int EXIST_ONLY_ONE_SECTION = 1;

    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(final String name, final String color, final List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = new ArrayList<>(sections);
    }

    public boolean containsAll(final Station start, final Station end) {
        return sections.stream()
                .anyMatch(section -> section.containsAll(start, end));
    }

    public void add(final Station base, final Station additional, final Distance distance, final Direction direction) {
        validate(base, additional, distance, direction);
        direction.addStation(sections, base, additional, distance);
    }

    private void validate(
            final Station base,
            final Station additional,
            final Distance distance,
            final Direction direction
    ) {
        if (!contains(base)) {
            throw new InvalidSectionException("기준역이 존재하지 않습니다.");
        }
        if (contains(additional)) {
            throw new InvalidSectionException("등록할 역이 이미 존재합니다.");
        }
        if (isNotValidDistanceToAddRight(base, distance, direction)
                || isNotValidDistanceToAddLeft(base, distance, direction)) {
            throw new InvalidSectionException("등록할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
        }
    }

    private boolean isNotValidDistanceToAddRight(
            final Station base,
            final Distance distance,
            final Direction direction
    ) {
        if (direction == LEFT) {
            return false;
        }
        final Optional<Section> findSection = findSection(section -> section.isStart(base));
        return findSection.map(section -> section.canNotAddStationInMiddle(distance))
                .orElse(false);
    }

    private boolean isNotValidDistanceToAddLeft(
            final Station base,
            final Distance distance,
            final Direction direction
    ) {
        if (direction == RIGHT) {
            return false;
        }
        final Optional<Section> findSection = findSection(section -> section.isEnd(base));
        return findSection.map(section -> section.canNotAddStationInMiddle(distance))
                .orElse(false);
    }

    private Optional<Section> findSection(final Function<Section, Boolean> filter) {
        return sections.stream()
                .filter(filter::apply)
                .findFirst();
    }

    private List<Section> findAllSections(final Function<Section, Boolean> filter) {
        return sections.stream()
                .filter(filter::apply)
                .collect(toList());
    }

    public void remove(final Station station) {
        if (!contains(station)) {
            throw new StationNotFoundException();
        }
        final List<Section> findSections = findAllSections(section -> section.contains(station));
        if (findSections.size() == EXIST_ONLY_ONE_SECTION) {
            sections.remove(findSections.get(0));
            return;
        }
        removeMiddleStation(findSections, station);
    }

    private void removeMiddleStation(final List<Section> findSections, final Station station) {
        final Section section = findSections.get(0);
        final Section otherSection = findSections.get(1);
        sections.remove(section);
        sections.remove(otherSection);
        if (section.isEnd(station)) {
            sections.add(new Section(section.getStart(), otherSection.getEnd(), section.add(otherSection.getDistance())));
            return;
        }
        sections.add(new Section(otherSection.getStart(), section.getEnd(), otherSection.add(section.getDistance())));
    }

    private boolean contains(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    public List<Station> findAllStation() {
        final Map<Station, Station> stationToStation = sections.stream()
                .collect(toMap(Section::getStart, Section::getEnd));

        final Optional<Station> firstStation = findFirstStation(stationToStation);
        return firstStation.map(station -> orderStations(stationToStation, station))
                .orElse(Collections.emptyList());
    }

    private Optional<Station> findFirstStation(final Map<Station, Station> stationToStation) {
        final Set<Station> stations = new HashSet<>(stationToStation.keySet());
        stations.removeAll(stationToStation.values());
        return stations.stream().findFirst();
    }

    private List<Station> orderStations(final Map<Station, Station> stationToStation, Station station) {
        final List<Station> result = new ArrayList<>(List.of(station));
        while (stationToStation.containsKey(station)) {
            final Station next = stationToStation.get(station);
            result.add(next);
            station = next;
        }
        return result;
    }

    public void initialAdd(final Station left, final Station right, final Distance distance) {
        if (!sections.isEmpty()) {
            throw new LineNotEmptyException();
        }
        sections.add(new Section(left, right, distance));
    }

    public boolean isSameName(final String lineName) {
        return name.equals(lineName);
    }

    public boolean hasStation(String startStationName) {
        return sections.stream()
                .anyMatch(section -> section.contains(startStationName));
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sections=" + sections +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
