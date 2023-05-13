package subway.domain;

import subway.exception.invalid.DistanceInvalidException;

import java.util.Objects;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final Long distance;

    public Section(final Station upStation, final Station downStation, final Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public boolean isExistStation(final Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public void validateDistance(final Long requestDistance) {
        if (this.distance <= requestDistance) {
            throw new DistanceInvalidException();
        }
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }
}
