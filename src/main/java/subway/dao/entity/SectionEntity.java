package subway.dao.entity;

import subway.domain.Direction;

public class SectionEntity {
    private final Long id;
    private final int distance;
    private final Long departureId;
    private final Long arrivalId;
    private final Long lineId;
    private final Direction direction;

    public SectionEntity(final Long id, final int distance, final Long departureId, final Long arrivalId, final Long lineId, final Direction direction) {
        this.id = id;
        this.distance = distance;
        this.departureId = departureId;
        this.arrivalId = arrivalId;
        this.lineId = lineId;
        this.direction = direction;
    }

    public SectionEntity(final int distance, final Long departureId, final Long arrivalId, final Long lineId, final Direction direction) {
        this.id = null;
        this.distance = distance;
        this.departureId = departureId;
        this.arrivalId = arrivalId;
        this.lineId = lineId;
        this.direction = direction;
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

    public Direction getDirection() {
        return direction;
    }
}
