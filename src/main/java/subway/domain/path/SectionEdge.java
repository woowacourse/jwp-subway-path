package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.Objects;

public class SectionEdge extends DefaultWeightedEdge {

    private final Long lineId;
    private final Section section;

    public SectionEdge(final Long lineId, final Section section) {
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

    public Distance getDistance() {
        return section.getDistance();
    }

    public int getDistanceValue() {
        return section.getDistanceValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SectionEdge that = (SectionEdge) o;
        return Objects.equals(lineId, that.lineId) && Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, section);
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
                "lineId=" + lineId +
                ", section=" + section +
                '}';
    }
}
