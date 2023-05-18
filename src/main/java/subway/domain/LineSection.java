package subway.domain;

public class LineSection {

    private final Long id;
    private final Station previousStation;
    private final Station nextStation;
    private final Distance distance;

    public LineSection(final Long id, final Station previousStation, final Station nextStation, final Distance distance) {
        this.id = id;
        this.previousStation = previousStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Station getPreviousStation() {
        return previousStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Distance getDistance() {
        return distance;
    }

}
