package subway.dao.entity;

public class SectionEntity {

    private final Long lineId;
    private final Long leftStationId;
    private final Long rightStationId;
    private final Integer distance;

    public SectionEntity(Long lineId, Long leftStationId, Long rightStationId, Integer distance) {
        this.lineId = lineId;
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public Long getRightStationId() {
        return rightStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
