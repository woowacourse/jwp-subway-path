package subway.entity;

public class SectionEntity {

    private final Long id;
    private final Long lineId;
    private final Long originId;
    private final Long destinationId;
    private final Integer distance;

    public SectionEntity(final Long id, final Long lineId, final Long originId, final Long destinationId,
            final Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.originId = originId;
        this.destinationId = destinationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getOriginId() {
        return originId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
