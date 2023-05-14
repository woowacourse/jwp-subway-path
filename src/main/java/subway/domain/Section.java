package subway.domain;

import java.util.Objects;

public class Section {
    private final Station up;
    private final Station down;
    private final Distance distance;

    public Section(Station up, Station down, Distance distance) {
        this.distance = distance;
        this.up = up;
        this.down = down;
    }

    public Section connectToUp(Station station, Distance distance) {
        return new Section(station, up, distance);
    }

    public Section connectToDown(Station station, Distance distance) {
        return new Section(down, station, distance);
    }

    public Distance subDistance(Distance distance) {
        return this.distance.sub(distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(distance, section.distance) && Objects.equals(up, section.up) && Objects.equals(down, section.down);
    }

    @Override
    public int hashCode() {
        return Objects.hash(up, down, distance);
    }

    public Station getUp() {
        return up;
    }

    public Station getDown() {
        return down;
    }

    public Distance getDistance() {
        return distance;
    }

}
