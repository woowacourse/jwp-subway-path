package subway.dto;

public final class SectionResponse {

    private Long lineId;
    private Long leftStationId;
    private Long rightStationId;
    private int distance;

    public SectionResponse(Long lineId, Long leftStationId, Long rightStationId, int distance) {
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

    public int getDistance() {
        return distance;
    }
}
