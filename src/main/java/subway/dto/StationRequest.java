package subway.dto;

import java.util.Objects;

public class StationRequest {
    private final String upStationName;
    private final String downStationName;
    private final int distance;
    private final Long lineId;

    public StationRequest(String upStationName, String downStationName, int distance, Long lineId) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
        this.lineId = lineId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationRequest request = (StationRequest) o;
        return distance == request.distance && Objects.equals(upStationName, request.upStationName) && Objects.equals(downStationName, request.downStationName) && Objects.equals(lineId, request.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationName, downStationName, distance, lineId);
    }
}
