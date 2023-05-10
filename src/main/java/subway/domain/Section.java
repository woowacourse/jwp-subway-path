package subway.domain;

import java.util.Objects;

import subway.exception.DomainException;
import subway.exception.ExceptionType;

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
        this.distance = validate(distance);
    }

    private Integer validate(Integer distance) {
        if (distance < 1) {
            throw new DomainException(ExceptionType.INVALID_DISTANCE);
        }
        return distance;
    }

    public boolean isSourceStation(Long sourceStationId) {
        return this.sourceStationId.equals(sourceStationId);
    }

    public boolean isTargetStation(Long targetStationId) {
        return this.targetStationId.equals(targetStationId);
    }

    public boolean containsTheseStations(Long sourceStationId, Long targetStationId) {
        return (isSourceStation(sourceStationId) && isTargetStation(targetStationId))
            || (isSourceStation(targetStationId) && isTargetStation(sourceStationId));
    }

    public boolean hasShorterOrSameDistanceThan(Integer distance) {
        return this.distance <= distance;
    }

    public boolean includeStation(Long stationId) {
        return isSourceStation(stationId) || isTargetStation(stationId);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
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
}
