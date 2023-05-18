package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class Section extends DefaultWeightedEdge {
    private final Long lindId;
    private final Station upper;
    private final Station lower;
    private final Distance distance;

    public Section(Long lindId, Station upper, Station lower, Distance distance) {
        this.lindId = lindId;
        this.upper = upper;
        this.lower = lower;
        this.distance = distance;
    }

    public boolean isNext(final Station station) {
        if (lower.equals(station)) {
            return true;
        }
        return false;
    }

    public Long getLindId() {
        return lindId;
    }

    public Station getUpper() {
        return upper;
    }

    public Station getLower() {
        return lower;
    }

    public Distance getDistance() {
        return distance;
    }
}
