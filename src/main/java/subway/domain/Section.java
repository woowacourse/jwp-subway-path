package subway.domain;

public class Section {

    private final Long id;
    private final Station beforeStation;
    private final Station nextStation;
    private final Distance distance;

    public Section(final Long id, final Station beforeStation, final Station nextStation, final Distance distance) {
        validateSameStation(beforeStation, nextStation);
        this.id = id;
        this.beforeStation = beforeStation;
        this.nextStation = nextStation;
        this.distance = distance;
    }

    private void validateSameStation(final Station beforeStation, final Station nextStation) {
        if (beforeStation.equals(nextStation)) {
            throw new IllegalArgumentException("구간은 서로 다른 두 역이어야 합니다.");
        }
    }

    public Section(final Station beforeStation, final Station nextStation, final Distance distance) {
        this(null, beforeStation, nextStation, distance);
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

    public boolean isSameStations(final Section section) {
        return (section.beforeStation == beforeStation && section.nextStation == nextStation)
                || (section.beforeStation == nextStation && section.nextStation == beforeStation);
    }
}
