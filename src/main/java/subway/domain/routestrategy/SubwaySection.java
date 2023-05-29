package subway.domain.routestrategy;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Distance;
import subway.domain.Section;

public class SubwaySection extends DefaultWeightedEdge {
    
    private final Section section;
    private final int charge;
    
    public SubwaySection(Section section, int charge) {
        this.section = section;
        this.charge = charge;
    }
    
    public Distance getDistance() {
        return section.getDistance();
    }
    
    public int getCharge() {
        return charge;
    }
}
