package subway.dto;

public class SectionRequest {
    private Long upperStationId;
    private Long lowerStationId;
    private Integer distance;

    private SectionRequest() {
    }

    public SectionRequest(Long upperStationId, Long lowerStationId, Integer distance) {
        this.upperStationId = upperStationId;
        this.lowerStationId = lowerStationId;
        this.distance = distance;
    }

    public Long getUpperStationId() {
        return upperStationId;
    }

    public Long getLowerStationId() {
        return lowerStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
