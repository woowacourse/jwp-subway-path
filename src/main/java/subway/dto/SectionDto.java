package subway.dto;

public class SectionDto {

    private Long lineId;
    private Long stationId;
    private Long nextStationId;
    private Integer distance;

    public SectionDto(Long lineId, Long stationId, Long nextStationId, Integer distance) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.nextStationId = nextStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
