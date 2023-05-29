package study;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.section.Section;
import subway.domain.station.Station;

public class SectionExample extends DefaultWeightedEdge {

    private final Long lineId;
    private final Section section;

    public SectionExample(final Long lineId, final Section section) {
        this.lineId = lineId;
        this.section = section;
    }

    public Section toSection() {
        return section;
    }

    public Long getLineId() {
        return lineId;
    }

    public Station getFrom() {
        return section.getFrom();
    }

    public Station getTo() {
        return section.getTo();
    }

    public Integer getDistance() {
        return section.getDistanceValue();
    }
}
