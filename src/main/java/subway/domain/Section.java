package subway.domain;

import subway.exceptions.IllegalStationException;
import subway.exceptions.SectionStateException;

import java.util.Objects;

public class Section {
    private final Station up;
    private final Station down;
    private final Distance distance;

    public Section(final Station up, final Station down, final Distance distance) {
        validateSameStation(up, down);
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    private void validateSameStation(final Station up, final Station down) {
        if (up.equals(down)) {
            throw new IllegalStationException("구간의 두 역이 같을 수 없습니다.");
        }
    }

    public Section connectToUp(final Station station, final Distance distance) {
        return new Section(station, up, distance);
    }

    public Section connectToDown(final Station station, final Distance distance) {
        return new Section(down, station, distance);
    }

    public Section connectIntermediate(final Station station, final Distance distance) {
        return new Section(up, station, distance);
    }

    public Distance subDistance(final Distance distance) {
        return this.distance.sub(distance);
    }

    public Section deleteStation(final Section upIsStation) {
        if (!down.equals(upIsStation.up)) {
            throw new SectionStateException("삭제하려는 두 구간이 이어져있지 않습니다.");
        }
        return new Section(up, upIsStation.down, distance.sum(upIsStation.distance));
    }

    public boolean contains(final Station station) {
        return up.equals(station) || down.equals(station);
    }

    public boolean isUp(final Station station) {
        return up.equals(station);
    }

    public boolean isDown(final Station station) {
        return down.equals(station);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
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
