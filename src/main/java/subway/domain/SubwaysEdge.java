package subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SubwaysEdge extends DefaultWeightedEdge {

    private final Line line;
    private final int weight;

    public SubwaysEdge(Line line, int weight) {
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
