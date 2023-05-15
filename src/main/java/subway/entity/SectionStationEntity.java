package subway.entity;

public class SectionStationEntity {

    private final Long id;
    private final Long leftStationId;
    private final String leftStationName;
    private final Long rightStationId;
    private final String rightStationName;
    private final int distance;

    public SectionStationEntity(Long id, Long leftStationId, String leftStationName, Long rightStationId, String rightStationName, int distance) {
        this.id = id;
        this.leftStationId = leftStationId;
        this.leftStationName = leftStationName;
        this.rightStationId = rightStationId;
        this.rightStationName = rightStationName;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public String getLeftStationName() {
        return leftStationName;
    }

    public Long getRightStationId() {
        return rightStationId;
    }

    public String getRightStationName() {
        return rightStationName;
    }

    public int getDistance() {
        return distance;
    }
}
