package subway.entity;

public class Section {

    private final Long id;
    private final Long leftStationId;
    private final Long rightStationId;
    private final int distance;

    public Section(final Long id, final Long leftStationId, final Long rightStationId, final int distance) {
        this.id = id;
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public Section(final Long leftStationId, final Long rightStationId, final int distance) {
        this(null, leftStationId, rightStationId, distance);
    }

    public Long getId() {
        return id;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public Long getRightStationId() {
        return rightStationId;
    }

    public int getDistance() {
        return distance;
    }
}
