package subway.domain;

import java.util.Objects;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(final Station upStation, final Station downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    @Override
    public int hashCode() {
        return Objects.hash(upStation.getId(), downStation.getId());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Section section = (Section) o;
        return upStation.getId().equals(section.upStation.getId()) && downStation.getId().equals(section.downStation.getId());
    }
}
