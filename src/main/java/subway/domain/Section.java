package subway.domain;

import subway.exception.DataNotFoundException;

import java.util.Objects;

public class Section {
    private final Long lindId;
    private final Station upper;
    private final Station lower;
    private final Distance distance;

    public Section(final Long lindId, final Station upper, final Station lower, final Distance distance) {
        this.lindId = lindId;
        this.upper = checkNull(upper);
        this.lower = checkNull(lower);
        this.distance = distance;
    }

    public boolean isNext(final Station station) {
        return lower.equals(station);
    }

    private Station checkNull(final Station station) {
        if (station == null) {
            throw new DataNotFoundException("존재하지 않는 역입니다");
        }
        return station;
    }

    public Long getLindId() {
        return lindId;
    }

    public Station getUpper() {
        return upper;
    }

    public Station getLower() {
        return lower;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(lindId, section.lindId) && Objects.equals(upper, section.upper) && Objects.equals(lower, section.lower) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lindId, upper, lower, distance);
    }
}
