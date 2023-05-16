package subway.domain;

public class Section {
    private final Station upper;
    private final Station lower;
    private final Distance distance;

    public Section(final Station upper, final Station lower, final Distance distance) {
        this.upper = upper;
        this.lower = lower;
        this.distance = distance;
    }

    public boolean isNext(final Station station) {
        if (lower.equals(station)) {
            return true;
        }
        return false;
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
}
