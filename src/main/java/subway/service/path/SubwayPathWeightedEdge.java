package subway.service.path;

import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Line;

@Getter
public class SubwayPathWeightedEdge extends DefaultWeightedEdge {
    private final Line line;

    public SubwayPathWeightedEdge(Line line) {
        this.line = line;
    }
}
