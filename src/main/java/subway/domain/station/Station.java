package subway.domain.station;

import java.util.Objects;

import subway.domain.line.Line;

/**
 * Station : Line -> N : 1
 * join을 하는 상황을 대비?해서 도메인 엔티티에 타입을 Line으로 가지고 있어도 되나?
 * 조회를 할 때, line_id 밖에 필요 없는데, Join을 해서 모든 열으로 Line을 만들어야 하는데 필요할까?
 * -> "단방향 연관관계!라고 한다!"
 */
public class Station {

    private final Long id;
    private final StationName name;
    private final Line line;

    public Station(Long id, String name, Line line) {
        this.id = id;
        this.name = new StationName(name);
        this.line = line;
    }

    public boolean isSameLine(Line targetLine) {
        return line.getId().equals(targetLine.getId());
    }

    public boolean isSameStationName(String targetStationName) {
        return name.getStationName().equals(targetStationName);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getStationName();
    }

    public Line getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(name, station.name) && Objects.equals(line, station.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, line);
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", name=" + name +
                ", line=" + line +
                '}';
    }
}
