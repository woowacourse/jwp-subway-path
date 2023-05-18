package subway.domain;

import java.util.List;
import subway.domain.charge.Charge;

public class Path {
    private final List<LineInPath> lines;
    private final double totalDistance;
    private final Charge totalCharge;

    public Path(List<LineInPath> lines, double totalDistance, Charge totalCharge) {
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

    public Charge getTotalCharge() {
        return totalCharge;
    }
}
