package subway.domain;

public class Section {

    private Long id;
    private final Station beforeStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(final Long id, final Station beforeStation, final Station nextStation, final Distance distance) {
        this(beforeStation, nextStation, distance);
        this.id = id;
    }

    public Section(final Station beforeStation, final Station nextStation, final Distance distance) {
        this.beforeStation = beforeStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public boolean isEqualBeforeStation(final Station station) {
        return station.equals(beforeStation);
    }

    public boolean isEqualNextStation(final Station station) {
        return station.equals(nextStation);
    }

    public Long getId() {
        return id;
    }

    public Station getBeforeStation() {
        return beforeStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Distance getDistance() {
        return distance;
    }
}
