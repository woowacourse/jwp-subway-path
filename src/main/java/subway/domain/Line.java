package subway.domain;

import java.util.Objects;

public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final Stations stations;

    public Line(String name, String color) {
        this(null, name, color);
    }

    public Line(Long id, String name, String color) {
        this(id, name, color, Stations.emptyStations());
    }

    public Line(Long id, String name, String color, Stations stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public void initStations(Station previousStation, Station nextStation) {
        if (stations.isEmpty()) {
            stations.addLast(previousStation);
            stations.addLast(nextStation);
            return;
        }
        throw new IllegalStateException("비어있지 않은 노선에 역을 1개만 추가할 수 있습니다.");
    }

    public void addBeforeAt(Station beforeStation, Station station) {
        validateEmpty();
        stations.addBeforeAt(beforeStation, station);
    }

    private void validateEmpty() {
        if (stations.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    public void addLast(Station station) {
        validateEmpty();
        stations.addLast(station);
    }

    public boolean isInitState() {
        return stations.isEmpty();
    }

    public boolean hasStation(Station station) {
        return stations.contains(station);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
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

    public Stations getStations() {
        return stations;
    }
}
