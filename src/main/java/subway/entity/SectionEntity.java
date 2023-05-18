package subway.entity;

import subway.exception.InvalidDistanceException;

import java.util.Objects;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Integer distance;
    private final Long previousStationId;
    private final Long nextStationId;

    public SectionEntity(final Long id, final Long lineId, final Integer distance,
                         final Long previousStationId, final Long nextStationId) {
        this.id = id;
        this.lineId = lineId;
        this.distance = distance;
        this.previousStationId = previousStationId;
        this.nextStationId = nextStationId;
    }

    public SectionEntity(final Long lineId, final Integer distance,
                         final Long previousStationId, final Long nextStationId) {
        this(null, lineId, distance, previousStationId, nextStationId);
    }

    public static SectionEntity of(final Long id, final SectionEntity sectionEntity) {
        return new SectionEntity(id,
                sectionEntity.lineId, sectionEntity.distance,
                sectionEntity.previousStationId, sectionEntity.nextStationId);
    }

    public static SectionEntity createBoundOf(final SectionEntity firstElement, final SectionEntity secondElement) {
        final int distance = firstElement.distance + secondElement.distance;
        if (firstElement.isPreviousOf(secondElement)) {
            return new SectionEntity(firstElement.lineId, distance, firstElement.previousStationId, secondElement.nextStationId);
        }
        if (secondElement.isPreviousOf(firstElement)) {
            return new SectionEntity(secondElement.lineId, distance, secondElement.previousStationId, firstElement.nextStationId);
        }
        throw new RuntimeException("연결할 수 없는 SectionEntity 입니다.");
    }

    private boolean isPreviousOf(final SectionEntity source) {
        return Objects.equals(nextStationId, source.previousStationId);
    }

    public SectionEntity getSplitPreviousBy(final SectionEntity splitNext) {
        final int subtractedDistance = subtractDistance(splitNext.distance);
        return new SectionEntity(lineId, subtractedDistance, previousStationId, splitNext.previousStationId);
    }

    public SectionEntity getSplitNextBy(final SectionEntity splitPrevious) {
        final int subtractedDistance = subtractDistance(splitPrevious.distance);
        return new SectionEntity(lineId, subtractedDistance, splitPrevious.nextStationId, nextStationId);
    }

    private int subtractDistance(final int subtraction) {
        if (distance > subtraction) {
            return distance - subtraction;
        }
        throw new InvalidDistanceException();
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getPreviousStationId() {
        return previousStationId;
    }

    public Long getNextStationId() {
        return nextStationId;
    }

    @Override
    public String toString() {
        return "SectionEntity{" +
                "id=" + id +
                ", lineId=" + lineId +
                ", distance=" + distance +
                ", previousStationId=" + previousStationId +
                ", nextStationId=" + nextStationId +
                '}';
    }
}
