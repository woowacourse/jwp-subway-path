package subway.domain;

public class Section {

    private final Station start;
    private final Station end;
    private final Distance distance;

    public Section(final String start, final String end, final int distance) {
        this.start = new Station(start);
        this.end = new Station(end);
        this.distance = new Distance(distance);
    }

    public boolean contains(final Station station) {
        return start.equals(station) || end.equals(station);
    }

    public boolean containsAll(final Station start, final Station end) {
        return (this.start.equals(start) && this.end.equals(end))
                || (this.start.equals(end) && this.end.equals(start));
    }

    public Station getStart() {
        return start;
    }

    public Station getEnd() {
        return end;
    }

    public Distance getDistance() {
        return distance;
    }
}
