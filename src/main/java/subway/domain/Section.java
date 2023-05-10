package subway.domain;

import java.util.Objects;

public class Section {

    private final Station from;
    private final Station to;
    private final Distance distance;

    public Section(final Station from, final Station to, final int distance) {
        validate(from, to);
        this.from = from;
        this.to = to;
        this.distance = new Distance(distance);
    }

    private void validate(final Station from, final Station to) {
        if (from.equals(to)) {
            throw new IllegalArgumentException("입력한 두 역은 같습니다.");
        }
    }

    public boolean exist(final Station station) {
        return existLeft(station) || existRight(station);
    }

    public boolean existLeft(final Station station) {
        return station.equals(from);
    }

    public boolean existRight(final Station station) {
        return station.equals(to);
    }

    public boolean isInsertable(final int otherDistance) {
        return distance.isLongerThan(otherDistance);
    }

    public Section changeLeft(Station otherStation, int otherDistance) {
        return new Section(otherStation, to, distance.subtract(otherDistance));
    }

    public Section changeRight(final Station otherStation, final int otherDistance) {
        return new Section(from, otherStation, distance.subtract(otherDistance));
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
        return Objects.equals(from, section.from) && Objects.equals(to, section.to) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, distance);
    }

    @Override
    public String toString() {
        return "Section{" +
                "from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }
}
