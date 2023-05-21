package subway.dto.request;

import subway.dto.LineCreateDto;

public class LineCreateRequest {
    private String lineName;
    private Integer extraCharge;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public LineCreateRequest(String lineName, Integer extraCharge, Long upStationId, Long downStationId,
                             Integer distance) {
        this.lineName = lineName;
        this.extraCharge = extraCharge;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineCreateDto toDto() {
        return new LineCreateDto(lineName, extraCharge, upStationId, downStationId, distance);
    }

    public String getLineName() {
        return lineName;
    }

    public Integer getExtraCharge() {
        return extraCharge;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
