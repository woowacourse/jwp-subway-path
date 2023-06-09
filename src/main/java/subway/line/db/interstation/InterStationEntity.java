package subway.line.db.interstation;

import subway.line.domain.interstation.InterStation;

public class InterStationEntity {

    private final Long id;
    private final Long lineId;
    private final long upStationId;
    private final long downStationId;
    private final long distance;

    public InterStationEntity(
            Long id, Long lineId, long upStationId, long downStationId, long distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static InterStationEntity of(InterStation interStation, Long lineId) {
        return new InterStationEntity(interStation.getId(),
                lineId,
                interStation.getUpStationId(),
                interStation.getDownStationId(),
                interStation.getDistanceValue());
    }

    public static InterStationEntity of(InterStationEntity interStationEntity, long lineId) {
        return new InterStationEntity(interStationEntity.getId(),
                lineId,
                interStationEntity.getUpStationId(),
                interStationEntity.getDownStationId(),
                interStationEntity.getDistance());
    }

    public InterStation toInterStation() {
        return new InterStation(id, upStationId, downStationId, distance);
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getDistance() {
        return distance;
    }
}
