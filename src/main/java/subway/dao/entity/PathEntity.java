package subway.dao.entity;

public final class PathEntity {
    private final Long stationId;
    private final String name;
    private final Long upStationId;
    private final Long distance;

    public PathEntity(final Long stationId, final String name, final Long upStationId, final Long distance) {
        this.stationId = stationId;
        this.name = name;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public PathEntity(final Long upStationId, final Long downStationId, final Long distance) {
        this(downStationId, null, upStationId, distance);
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "PathEntity{" +
                "stationId=" + stationId +
                ", name='" + name + '\'' +
                ", upStationId=" + upStationId +
                ", distance=" + distance +
                '}';
    }
}
