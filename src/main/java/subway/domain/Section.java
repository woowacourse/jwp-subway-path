package subway.domain;

import java.util.Objects;

public class Section {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final long distance;

    public Section(final Long id, final Station upStation, final Station downStation, final long distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(final Station upStation, final Station downStation, final long distance) {
        this(null, upStation, downStation, distance);
    }

    public boolean bothStationsEquals(final Station upStation, final Station downStation) {
        return this.upStation.equals(upStation)
            && this.downStation.equals(downStation);
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
        if (id == null || section.id == null) {
            return Objects.equals(getUpStation(), section.getUpStation())
                && Objects.equals(getDownStation(), section.getDownStation());
        }
        return Objects.equals(id, section.id) && Objects.equals(getUpStation(), section.getUpStation())
            && Objects.equals(getDownStation(), section.getDownStation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getUpStation(), getDownStation());
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
