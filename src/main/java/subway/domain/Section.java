package subway.domain;

import subway.exception.GlobalException;

import java.util.Objects;

public class Section {
    private final Station startStation;
    private final Station endStation;
    private final Distance distance;

    public Section(Station startStation, Station endStation, Distance distance) {
        validate(startStation, endStation);

        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    private void validate(Station startStation, Station endStation) {
        if (startStation.equals(endStation)) {
            throw new GlobalException("시작 역과 도착 역은 같을 수 없습니다.");
        }
    }

    public boolean isSameStartStation(Section otherSection) {
        return this.isSameStartStation(otherSection.startStation);
    }

    public boolean isSameStartStation(Station otherStation) {
        return this.startStation.equals(otherStation);
    }

    public boolean isSameEndStation(Section otherSection) {
        return this.isSameEndStation(otherSection.endStation);
    }

    public boolean isSameEndStation(Station otherStation) {
        return this.endStation.equals(otherStation);
    }

    public boolean isGreaterThanOtherDistance(Section otherSection) {
        return this.distance.isBiggerThan(otherSection.distance);
    }

    public boolean hasStation(Station otherStation) {
        return isSameStartStation(otherStation) || isSameEndStation(otherStation);
    }

    public Distance addDistance(Section otherSection) {
        return this.distance.add(otherSection.distance);
    }

    public Distance subtractDistance(Section otherSection) {
        return this.distance.subtract(otherSection.distance);
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(startStation, section.startStation) && Objects.equals(endStation,
                section.endStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startStation, endStation);
    }
}
