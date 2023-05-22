package subway.domain;

import java.util.Objects;
import subway.exception.invalid.DistanceInvalidException;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final Long distance;

    public Section(final Station upStation, final Station downStation, final Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void validateDistance(final Long otherDistance) {
        if (this.distance <= otherDistance) {
            throw new DistanceInvalidException();
        }
    }

    public boolean contains(final Station station) {
        return upStation.equals(station) || downStation.equals(station);
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
        if (this == o) {
            return true;
        }
        if (!(o instanceof Section)) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(upStation, section.upStation) && Objects.equals(downStation, section.downStation) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }
}
