package subway.domain.section;

import subway.domain.line.Fare;
import subway.domain.station.Station;
import subway.domain.subway.LineWeightedEdge;

public class PathSection {

    private final Long lineId;
    private final Station source;
    private final Station target;
    private final Distance distance;
    private final Fare fareOfLine;

    public PathSection(
            final Long lineId,
            final Station source,
            final Station target,
            final int distance,
            final int fareOfLine
    ) {
        this.lineId = lineId;
        this.source = source;
        this.target = target;
        this.distance = new Distance(distance);
        this.fareOfLine = new Fare(fareOfLine);
    }

    public static PathSection from(final LineWeightedEdge edge) {
        return new PathSection(
                edge.getLineId(),
                edge.getSource(),
                edge.getTarget(),
                (int) edge.getWeight(),
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

    public int getDistance() {
        return distance.getValue();
    }

    public int getFareOfLine() {
        return fareOfLine.getValue();
    }
}
