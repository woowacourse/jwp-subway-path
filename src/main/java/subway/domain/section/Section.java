package subway.domain.section;

import java.util.Objects;
import subway.domain.station.Station;
import subway.exception.invalid.DistanceInvalidException;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Long distance;

    public Section(final Station upStation, final Station downStation, final Long distance) {
        this(null, upStation, downStation, distance);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final Long distance) {
        this.id = id;
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
        return upStation.getName().equals(station.getName()) || downStation.getName().equals(station.getName());
    }

    public Long getId() {
        return id;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
