package subway.domain;

import java.util.Objects;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionForEdge extends DefaultWeightedEdge {

    private final Long id;
    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    public SectionForEdge(final Long id, final Long lineId, final Long upStationId, final Long downStationId,
                          final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionForEdge of(Section section) {
        return new SectionForEdge(section.getId(), section.getLineId(), section.getUpStationId(),
                section.getDownStationId(), section.getDistance());
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    @Override
    protected Long getSource() {
        return upStationId;
    }

    @Override
    protected Long getTarget() {
        return downStationId;
    }

    @Override
    protected double getWeight() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionForEdge section = (SectionForEdge) o;
        return Objects.equals(id, section.id) && Objects.equals(lineId, section.lineId)
                && Objects.equals(upStationId, section.upStationId) && Objects.equals(downStationId,
                section.downStationId) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineId, upStationId, downStationId, distance);
    }
}
