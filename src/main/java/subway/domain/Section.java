package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.exception.ErrorCode;
import subway.exception.InvalidException;

public class Section extends DefaultWeightedEdge {
    private final Station startStation;
    private final Station endStation;
    private final int distance;
    private final Line line;

    public Section(final Station startStation, final Station endStation, final int distance, final Line line) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
        this.line = line;
    }

    public boolean isSameStartStation(final Station station) {
        return startStation.equals(station);
    }

    public boolean isSameEndStation(final Station station) {
        return endStation.equals(station);
    }

    public void validateDistance(final int distance) {
        if (this.distance <= distance) {
            throw new InvalidException(ErrorCode.INVALID_DISTANCE);
        }
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }
}
