package subway.domain.section;

import subway.domain.station.Station;
import subway.domain.subway.LineWeightedEdge;

public class PathSection {

    private final Long lineId;
    private final Station source;
    private final Station target;
    private final double distance;
    private final int fareOfLine;

    public PathSection(
            final Long lineId,
            final Station source,
            final Station target,
            final double distance,
            final int fareOfLine
    ) {
        this.lineId = lineId;
        this.source = source;
        this.target = target;
        this.distance = distance;
        this.fareOfLine = fareOfLine;
    }

    public static PathSection from(final LineWeightedEdge edge) {
        return new PathSection(
                edge.getLineId(),
                edge.getSource(),
                edge.getTarget(),
                edge.getWeight(),
                edge.getFareOfLine()
        );
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

    public double getDistance() {
        return distance;
    }

    public int getFareOfLine() {
        return fareOfLine;
    }
}
