package subway.business.domain.subwaymap;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.business.domain.line.Station;

public class StationEdge extends DefaultWeightedEdge {

    public StationEdge() {
    }

    public Station getSource() {
        return (Station) super.getSource();
    }

    public Station getTarget() {
        return (Station) super.getTarget();
    }

    public double getWeight() {
        return super.getWeight();
    }
}
