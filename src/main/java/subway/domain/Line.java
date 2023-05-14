package subway.domain;

import static subway.domain.Direction.LEFT;
import static subway.domain.Direction.RIGHT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import subway.exception.InvalidSectionException;
import subway.exception.LineNotEmptyException;
import subway.exception.StationNotFoundException;

public class Line {

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

        if (direction == RIGHT) {
            final Optional<Section> sectionAtStart = findSectionByStationExistsAtDirection(base, LEFT);
            if (sectionAtStart.isPresent()) {
                final Section originSection = sectionAtStart.get();
                changeDistance(additional, originSection.getEnd(), originSection, distance);
            }
            sections.add(new Section(base, additional, distance));
        }

        if (direction == LEFT) {
            final Optional<Section> sectionAtEnd = findSectionByStationExistsAtDirection(base, RIGHT);
            if (sectionAtEnd.isPresent()) {
                final Section originSection = sectionAtEnd.get();
                changeDistance(originSection.getStart(), additional, originSection, distance);
            }
            sections.add(new Section(additional, base, distance));
        }
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
        if (isInvalidDistance(base, distance, direction)) {
            throw new InvalidSectionException("등록할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
        }
    }

    private boolean contains(final Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private boolean isInvalidDistance(final Station base, final Distance distance, final Direction direction) {
        return findSectionByStationExistsAtDirection(base, direction.flip())
                .map(section -> section.moreThanOrEqual(distance))
                .orElse(false);
    }

    private Optional<Section> findSectionByStationExistsAtDirection(final Station base, final Direction direction) {
        return sections.stream()
                .filter(section -> section.isStationExistsAtDirection(base, direction))
                .findFirst();
    }

    private void changeDistance(
            final Station start,
            final Station end,
            final Section originSection,
            final Distance distance
    ) {
        sections.add(new Section(start, end, originSection.subtract(distance)));
        sections.remove(originSection);
    }

    public boolean isSameName(final String lineName) {
        return name.equals(lineName);
    }

    public void remove(final Station station) {
        if (!contains(station)) {
            throw new StationNotFoundException();
        }

        final Optional<Section> sectionAtLeft = findSectionByStationExistsAtDirection(station, RIGHT);
        final Optional<Section> sectionAtRight = findSectionByStationExistsAtDirection(station, LEFT);

        if (sectionAtLeft.isPresent() && sectionAtRight.isPresent()) {
            final Section left = sectionAtLeft.get();
            final Section right = sectionAtRight.get();
            sections.add(new Section(left.getStart(), right.getEnd(), left.add(right.getDistance())));
        }

        sectionAtLeft.ifPresent(sections::remove);
        sectionAtRight.ifPresent(sections::remove);
    }

    public List<Station> findAllStation() {
        final Map<Station, Station> stationToStation = sections.stream()
                .collect(Collectors.toMap(Section::getStart, Section::getEnd));

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

    public void initialAdd(final Section section) {
        if (!sections.isEmpty()) {
            throw new LineNotEmptyException();
        }
        sections.add(section);
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
