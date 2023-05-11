package subway.section.domain;

import subway.station.domain.Station;

public class Section {

    private final Station up;
    private final Station down;
    private final int distance;

    private Section(final Station up, final Station down, final int distance) {
        this.up = up;
        this.down = down;
        this.distance = distance;
    }

    public static Section of(final Station up, final Station down, final int weight) {
        validateWeight(weight);
        return new Section(up, down, weight);
    }

    private static void validateWeight(final int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("섹션 사이의 거리는 0보다 작거나 같을 수 없습니다.");
        }
    }

    public Station getUp() {
        return up;
    }

    public Station getDown() {
        return down;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isStartWith(final Station station) {
        return up.equals(station);
    }

    public boolean isEndWith(final Station station) {
        return down.equals(station);
    }

    public boolean isContainStation(final Station station) {
        return isStartWith(station) || isEndWith(station);
    }

    @Override
    public String toString() {
        return "Section{" +
            "up=" + up +
            ", down=" + down +
            ", distance=" + distance +
            '}';
    }
}
