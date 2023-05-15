package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long leftStationId;
    private final Long rightStationId;
    private final int distance;

    public SectionEntity(final Long id, Long lineId, final Long leftStationId, final Long rightStationId, final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public SectionEntity(final Long leftStationId, final Long rightStationId, final int distance, Long lineId) {
        this(null, lineId, leftStationId, rightStationId, distance);
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

    public Long getLineId() {
        return lineId;
    }

    public int getDistance() {
        return distance;
    }
}
