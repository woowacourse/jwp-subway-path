package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Line {

    private final String name;
    private final String color;
    private final List<Section> sections;

    public Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = new LinkedList<>(sections);
    }

    public void addStation(Station baseStation, Station newStation, Direction directionOfBase, Distance newDistance) {
        validateSectionToAdd(baseStation, newStation, directionOfBase, newDistance);
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
        Optional<Section> sectionToRevise = findSectionIncludingBaseStationOnDirection(baseStation, directionOfBase);
        return sectionToRevise
                .map(section -> section.isLongerThan(newDistance))
                .orElse(true);
    }

    private Optional<Section> findSectionIncludingBaseStationOnDirection(Station baseStation, Direction direction) {
        return sections.stream()
                .filter(section -> section.isStationOnDirection(baseStation, direction))
                .findFirst();
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

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
