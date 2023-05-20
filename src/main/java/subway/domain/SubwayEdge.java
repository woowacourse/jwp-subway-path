package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwayEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int weight;

    public SubwayEdge(Line line, int weight) {
        this.line = line;
        this.weight = weight;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "SubwayEdge{" +
                "line=" + line +
                ", distance=" + weight +
                '}';
    }
}
