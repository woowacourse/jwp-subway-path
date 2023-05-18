package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {

    private final Long lindId;
    private final Station upper;
    private final Station lower;
    private final Distance distance;

    public SectionEdge(final Long lindId, final Station upper, final Station lower, final Distance distance) {
        this.lindId = lindId;
        this.upper = upper;
        this.lower = lower;
        this.distance = distance;
    }

    public static SectionEdge from(final Section section) {
        return new SectionEdge(section.getLindId(), section.getUpper(), section.getLower(), section.getDistance());
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

    @Override
    public double getWeight() {
        return distance.getValue();
    }
}
