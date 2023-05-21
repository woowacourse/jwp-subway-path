package subway.dto;

import java.util.Objects;

public class PathRequest {

    private final String startStationName;
    private final String endStationName;

    public PathRequest(String startStationName, String endStationName) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathRequest that = (PathRequest) o;
        return Objects.equals(startStationName, that.startStationName) && Objects.equals(endStationName, that.endStationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startStationName, endStationName);
    }

    @Override
    public String toString() {
        return "PathRequest{" +
                "startStationName='" + startStationName + '\'' +
                ", endStationName='" + endStationName + '\'' +
                '}';
    }
}
