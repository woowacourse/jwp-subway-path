package subway.domain;

import java.util.List;

public class Path {
    private final List<LineInPath> lines;
    private final double totalDistance;
    private final double totalCharge;

    public Path(List<LineInPath> lines, double totalDistance, double totalCharge) {
        this.lines = lines;
        this.totalDistance = totalDistance;
        this.totalCharge = totalCharge;
    }

    public List<LineInPath> getLines() {
        return lines;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public double getTotalCharge() {
        return totalCharge;
    }
}
