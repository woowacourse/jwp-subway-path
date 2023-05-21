package subway.dto;

import java.util.List;
import java.util.Objects;

public class PathResponse {

    private final List<String> stationNames;
    private final int distance;
    private final int fare;

    public PathResponse(List<String> stationNames, int distance, int fare) {
        this.stationNames = stationNames;
        this.distance = distance;
        this.fare = fare;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathResponse that = (PathResponse) o;
        return distance == that.distance && fare == that.fare && Objects.equals(stationNames, that.stationNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationNames, distance, fare);
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stationNames=" + stationNames +
                ", distance=" + distance +
                ", fare=" + fare +
                '}';
    }
}
