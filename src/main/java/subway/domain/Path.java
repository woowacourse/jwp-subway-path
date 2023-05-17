package subway.domain;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final double totalDistance;
    private final double totalCharge;

    public Path(List<Station> stations, double totalDistance, double totalCharge) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getTotalCharge() {
        return totalCharge;
    }

    @Override
    public String toString() {
        return "Path{" +
                "route=" + stations +
                ", totalDistance=" + totalDistance +
                ", totalCharge=" + totalCharge +
                '}';
    }
}
