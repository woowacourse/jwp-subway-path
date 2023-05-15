package subway.domain;

import static java.util.Collections.EMPTY_LIST;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Line {

    private static final int MIN_LENGTH_NAME = 1;
    private static final int MAX_LENGTH_NAME = 10;

    private final Long id;
    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(Long id, String name, String color, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        validateNameLength(name);
    }

    private void validateNameLength(String name) {
        if(name.length()<MIN_LENGTH_NAME || name.length() > MAX_LENGTH_NAME) {
            throw new IllegalArgumentException("노선 이름은 1 ~ 10자여야 합니다");
        }
    }

    public void addStation(String baseStationName, String newStationName, String direction, Integer distance) {
        Station baseStation = new Station(null, baseStationName);
        Station newStation = new Station(null, newStationName);
        Direction directionOfBase = Direction.findDirection(direction);
        Distance newDistance = new Distance(distance);
        if (sections.isEmpty()) {
            addInitialStations(baseStation, newStation, directionOfBase, newDistance);
            return;
        }
        addNewStation(baseStation, newStation, directionOfBase, newDistance);
    }

    public void addInitialStations(Station baseStation, Station newStation, Direction directionOfBase,
            Distance newDistance) {
        validateInitialSectionToAdd(baseStation, newStation);
        if (directionOfBase == Direction.LEFT) {
            sections.add(new Section(baseStation, newStation, newDistance));
        }
        if (directionOfBase == Direction.RIGHT) {
            sections.add(new Section(newStation, baseStation, newDistance));
        }
    }

    private void validateInitialSectionToAdd(Station baseStation, Station newStation) {
        if (!sections.isEmpty()) {
            throw new IllegalArgumentException("이미 등록된 역이 있습니다.");
        }
        if (baseStation.equals(newStation)) {
            throw new IllegalArgumentException("구간은 서로 다른 두 역으로 이뤄져 있어야 합니다.");
        }
    }

    public void addNewStation(Station baseStation, Station newStation, Direction directionOfBase,
            Distance newDistance) {
        validateSectionToAdd(baseStation, newStation, directionOfBase, newDistance);
        if (directionOfBase == Direction.LEFT) {
            addNewStationToRight(baseStation, newStation, newDistance, directionOfBase);
        }
        if (directionOfBase == Direction.RIGHT) {
            addNewStationToLeft(baseStation, newStation, newDistance, directionOfBase);
        }
    }

    private void validateSectionToAdd(Station baseStation, Station newStation, Direction directionOfBase,
            Distance newDistance) {
        if (!isExistInLine(baseStation)) {
            throw new IllegalArgumentException("기준역이 존재하지 않습니다");
        }
        if (isExistInLine(newStation)) {
            throw new IllegalArgumentException("추가하려는 역이 이미 존재합니다");
        }
        if (isExistingSectionIsLongerThanNewSection(baseStation, directionOfBase, newDistance)) {
            throw new IllegalArgumentException("새로운 구간의 거리는 기존 구간의 거리보다 짧아야 합니다.");
        }
    }

    private boolean isExistInLine(Station station) {
        return sections.stream()
                .anyMatch(section -> section.hasStationInSection(station));
    }

    private boolean isExistingSectionIsLongerThanNewSection(Station baseStation, Direction directionOfBase,
            Distance newDistance) {
        Optional<Section> sectionToRevise = findSectionIncludingStationOnDirection(baseStation, directionOfBase);
        return sectionToRevise
                .map(section -> section.isLongerThan(newDistance))
                .orElse(true);
    }

    private Optional<Section> findSectionIncludingStationOnDirection(Station baseStation, Direction direction) {
        return sections.stream()
                .filter(section -> section.isStationOnDirection(baseStation, direction))
                .findFirst();
    }

    private void addNewStationToRight(Station baseStation, Station newStation, Distance newDistance,
            Direction directionOfBase) {
        Optional<Section> sectionToRevise = findSectionIncludingStationOnDirection(baseStation, directionOfBase);
        if (sectionToRevise.isPresent()) {
            Section origin = sectionToRevise.get();
            sections.remove(origin);
            sections.add(new Section(newStation, origin.getDownStation(),
                    origin.getDistance().subtract(newDistance)));
            sections.add(new Section(baseStation, newStation, newDistance));
            return;
        }
        sections.add(new Section(baseStation, newStation, newDistance));
    }

    private void addNewStationToLeft(Station baseStation, Station newStation, Distance newDistance,
            Direction directionOfBase) {
        Optional<Section> sectionToRevise = findSectionIncludingStationOnDirection(baseStation, directionOfBase);
        if (sectionToRevise.isPresent()) {
            Section origin = sectionToRevise.get();
            sections.remove(origin);
            sections.add(new Section(origin.getUpStation(), newStation, origin.getDistance().subtract(newDistance)));
            sections.add(new Section(newStation, baseStation, newDistance));
            return;
        }
        sections.add(new Section(newStation, baseStation, newDistance));
    }

    public void removeStation(Station stationToRemove) {
        if (!isExistInLine(stationToRemove)) {
            throw new IllegalArgumentException("삭제하려는 역이 존재하지 않습니다");
        }
        Optional<Section> upSectionOpt = findSectionIncludingStationOnDirection(stationToRemove, Direction.RIGHT);
        Optional<Section> downSectionOpt = findSectionIncludingStationOnDirection(stationToRemove, Direction.LEFT);

        if (upSectionOpt.isPresent() && downSectionOpt.isPresent()) {
            Section upSection = upSectionOpt.get();
            Section downSection = downSectionOpt.get();
            Distance newDistance = upSection.getDistance().plus(downSection.getDistance());
            sections.removeAll(List.of(upSection, downSection));
            sections.add(new Section(upSection.getUpStation(), downSection.getDownStation(), newDistance));
            return;
        }
        upSectionOpt.ifPresent(sections::remove);
        downSectionOpt.ifPresent(sections::remove);
    }

    public List<Station> findAllStations() {
        if(sections.isEmpty()) {
            return EMPTY_LIST;
        }
        Station startStation = findFirstStation();
        List<Station> stations = new LinkedList<>(List.of(startStation));
        while (stations.size() <= sections.size()) {
            sections.stream()
                    .filter(section -> stations.get(stations.size() - 1).equals(section.getUpStation()))
                    .findFirst()
                    .ifPresent(section -> stations.add(section.getDownStation()));
        }
        return stations;
    }

    private Station findFirstStation() {
        Map<Station, Station> upAndDownStation = sections.stream()
                .collect(Collectors.toMap(Section::getUpStation, Section::getDownStation));
        Set<Station> stations = upAndDownStation.keySet();
        stations.removeAll(upAndDownStation.values());
        return stations.stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("상행 종점을 찾을 수 없습니다"));
    }

    public List<Section> findSectionsByStationName(String stationName) {
        return sections.stream()
                .filter(section -> section.hasStationInSection(new Station(null, stationName)))
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line line = (Line) o;
        return Objects.equals(name, line.name) || Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return new LinkedList<>(sections);
    }

}
