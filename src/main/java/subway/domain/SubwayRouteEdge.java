package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayRouteEdge extends DefaultWeightedEdge {
    private final Line line;
    private final Station sourceStation;
    private final Station targetStation;
    private final int distance;

    private SubwayRouteEdge(final Line line, final Station sourceStation, final Station targetStation, final int distance) {
        this.line = line;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public static SubwayRouteEdge of(final Line line, final Section section) {
        return new SubwayRouteEdge(line, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    protected double getWeight() {
        return distance;
    }
}
