package subway.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Line {
    private final Long id;
    private final String name;
    private final List<Section> sections;

    public Line(String name, List<Section> sections) {
        this.id = null;
        this.name = name;
        this.sections = sections;
    }

    public Line(Long id, String name, List<Section> sections) {
        this.id = id;
        this.name = name;
        this.sections = sections;
    }

    public static Line of(String name, String upwardStationName, String downwardStationName, int distance) {
        Station upwardStation = new Station(upwardStationName);
        Station downwardStation = new Station(downwardStationName);
        List<Section> sections = new LinkedList<>();
        sections.add(new Section(upwardStation, downwardStation, distance));
        return new Line(name, sections);
    }

    public void addStation(String stationName, String neighborhoodStationName, Direction direction, int distance) {
        // TODO 로직 분리
        Station station = new Station(stationName);
        Station neighborhoodStation = new Station(neighborhoodStationName);

        if (isStationExist(station)) {
            throw new IllegalArgumentException(String.format(
                    "이미 노선에 존재하는 역은 추가할 수 없습니다." + System.lineSeparator() +
                            "추가하려는 노선 : %s" + System.lineSeparator() +
                            "추가하려는 역 : %s", name, station.getName()));
        }
    }

    private boolean isStationExist(Station station) {
        for (Section section : sections) {
            if (section.hasStation(station)) {
                return true;
            }
        }
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return Objects.equals(id, line.id) && Objects.equals(name, line.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
