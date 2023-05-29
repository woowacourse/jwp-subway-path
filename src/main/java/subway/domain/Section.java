package subway.domain;

import java.util.Objects;

public class Section {
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    private Section(final Station upStation, final Station downStation, final Distance distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section of(Station upStation, Station downStation, int distance) {
        return new Section(upStation, downStation, new Distance(distance));
    }

    public static Section of(Station upStation, Station downStation, Distance distance) {
        return new Section(upStation, downStation, distance);
    }

    public static Section of(String upStationName, String downStationName, int distance) {
        return new Section(new Station(upStationName), new Station(downStationName), new Distance(distance));
    }

    public boolean equalsUpStation(Section section) {
        return this.upStation.equalsName(section.upStation);
    }

    public boolean equalsDownStation(Section section) {
        return this.downStation.equalsName(section.downStation);
    }

    public boolean isNextSection(Section section) {
        return this.downStation.equalsName(section.upStation);
    }

    public boolean isConnectableDifferentDirection(Section section) {
        return this.downStation.equalsName(section.upStation) || this.upStation.equalsName(section.downStation);
    }

    public boolean contains(Station station) {
        return upStation.equalsName(station) || downStation.equalsName(station);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation,
                section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }
}
