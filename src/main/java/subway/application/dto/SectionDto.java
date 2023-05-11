package subway.application.dto;

public class SectionDto {

    private Long lineId;
    private Long leftStationId;
    private Long rightStationId;
    private Integer distance;

    public SectionDto(Long lineId, Long leftStationId, Long rightStationId, Integer distance) {
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
