package subway.dto;

import java.util.Objects;

public class StationRequest {
    private final String upStationName;
    private final String downStationName;
    private final int distance;
    private final String lineName;

    public StationRequest(String upStationName, String downStationName, int distance, String lineName) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
        this.lineName = lineName;
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

    public String getLineName() {
        return lineName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationRequest that = (StationRequest) o;
        return distance == that.distance && Objects.equals(upStationName, that.upStationName) && Objects.equals(downStationName, that.downStationName) && Objects.equals(lineName, that.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationName, downStationName, distance, lineName);
    }
}
