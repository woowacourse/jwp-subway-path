package subway.domain;

public class Section {

    private final Long id;
    private final Station prevStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(final Long id, final Station prevStation, final Station nextStation, final Distance distance) {
        validateSameStation(prevStation, nextStation);
        this.id = id;
        this.prevStation = prevStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    private void validateSameStation(final Station prevStation, final Station nextStation) {
        if (prevStation.equals(nextStation)) {
            throw new IllegalArgumentException("구간은 서로 다른 두 역이어야 합니다.");
        }
    }

    public Section(final Station prevStation, final Station nextStation, final Distance distance) {
        this(null, prevStation, nextStation, distance);
    }

    public boolean isEqualPrevStation(final Station station) {
        return station.equals(prevStation);
    }

    public boolean isEqualNextStation(final Station station) {
        return station.equals(nextStation);
    }

    public Long getId() {
        return id;
    }

    public Station getPrevStation() {
        return prevStation;
    }

    public Station getNextStation() {
        return nextStation;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean containStation(final Station station) {
        return isEqualPrevStation(station) || isEqualNextStation(station);
    }
}
