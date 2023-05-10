package subway.domain;

import java.util.Objects;

public class Section {
    private final Long id;
    private final Long sourceStationId;
    private final Long targetStationId;
    private final Long lineId;
    private final Integer distance;

    public Section(Long id, Long sourceStationId, Long targetStationId, Long lineId, Integer distance) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.lineId = lineId;
        this.distance = distance;
    }

    public boolean isSrc(Long sourceStationId) {
        return this.sourceStationId.equals(sourceStationId);
    }

    public boolean isTar(Long targetStationId) {
        return this.targetStationId.equals(targetStationId);
    }

    public boolean containsTheseStations(Long sourceStationId, Long targetStationId) {
        return (isSrc(sourceStationId) && isTar(targetStationId))
            || (isSrc(targetStationId) && isTar(sourceStationId));
    }

    public boolean hasShorterOrSameDistanceThan(Integer distance) {
        return this.distance <= distance;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getLine() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Section section = (Section)o;
        return Objects.equals(id, section.id) && Objects.equals(sourceStationId,
            section.sourceStationId) && Objects.equals(targetStationId, section.targetStationId)
            && Objects.equals(lineId, section.lineId) && Objects.equals(distance, section.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceStationId, targetStationId, lineId, distance);
    }

    public boolean includeStation(Long stationId) {
        return isSrc(stationId) || isTar(stationId);
    }
}
