package subway.entity;

import subway.domain.Section;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long leftStationId;
    private final Long rightStationId;
    private final int distance;

    public SectionEntity(
            final Long id,
            final Long lineId,
            final Long leftStationId,
            final Long rightStationId,
            final int distance) {
        this.id = id;
        this.lineId = lineId;
        this.leftStationId = leftStationId;
        this.rightStationId = rightStationId;
        this.distance = distance;
    }

    public static SectionEntity of(final Long savedId, final SectionEntity sectionEntity) {
        return new SectionEntity(
                savedId,
                sectionEntity.getLineId(),
                sectionEntity.getLeftStationId(),
                sectionEntity.getRightStationId(),
                sectionEntity.getDistance()
        );
    }

    public static SectionEntity of(final Long lineId, final Section section) {
        return new SectionEntity(
                null,
                lineId,
                section.getLeft().getId(),
                section.getRight().getId(),
                section.getDistance()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getLeftStationId() {
        return leftStationId;
    }

    public Long getRightStationId() {
        return rightStationId;
    }

    public int getDistance() {
        return distance;
    }
}
