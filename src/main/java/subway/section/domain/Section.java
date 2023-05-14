package subway.section.domain;

import subway.station.domain.Station;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final int distance;

    private Section(final Station upStation, final Station downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(final Station upStation, final Station downStation, final int weight) {
        validateWeight(weight);
        return new Section(upStation, downStation, weight);
    }

    private static void validateWeight(final int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("역 사이의 거리는 0보다 작거나 같을 수 없습니다.");
        }
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isStartWith(final Station station) {
        return upStation.equals(station);
    }

    public boolean isEndWith(final Station station) {
        return downStation.equals(station);
    }

    public boolean isContainStation(final Station station) {
        return isStartWith(station) || isEndWith(station);
    }
}
