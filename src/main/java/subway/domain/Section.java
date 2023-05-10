package subway.domain;

public class Section {
    private final int distance;
    private final Long departureId;
    private final Long arrivalId;
    private final Direction direction;

    public Section(final int distance, final Long departureId, final Long arrivalId, final Direction direction) {
        this.distance = distance;
        this.departureId = departureId;
        this.arrivalId = arrivalId;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }
}
