package subway.domain;

import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
public class SubwayPathWeightedEdge extends DefaultWeightedEdge {
    private final Line line;

    public SubwayPathWeightedEdge(Line line) {
        this.line = line;
    }
}
