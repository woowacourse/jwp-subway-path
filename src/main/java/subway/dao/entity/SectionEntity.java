package subway.dao.entity;

public class SectionEntity {
    private final Long id;
    private final int distance;
    private final Long departureId;
    private final Long arrivalId;
    private final Long lineId;

    public SectionEntity(final Long id, final int distance, final Long departureId, final Long arrivalId, final Long lineId) {
        this.id = id;
        this.distance = distance;
        this.departureId = departureId;
        this.arrivalId = arrivalId;
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }

    public Long getLineId() {
        return lineId;
    }
}
