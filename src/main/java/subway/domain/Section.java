package subway.domain;

import java.util.Objects;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Station upStation, Station downStation, Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean hasStationInSection(Station station) {
        if(station.equals(upStation) || station.equals(downStation)) {
            return true;
        }
        return false;
    }

    public boolean isLongerThan(Distance other) {
        return distance.isLongerThan(other);
    }

    public boolean isStationOnDirection(Station baseStation, Direction direction) {
        if(direction == Direction.UP) {
            return upStation.equals(baseStation);
        }
        if(direction == Direction.DOWN) {
            return downStation.equals(baseStation);
        }
        return false;
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
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation,
                section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }

    @Override
    public String toString() {
        return "Section{" +
                "upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                '}';
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }
}
