package subway.dao.entity;

public final class PathEntity {
    private final Long stationId;
    private final String name;
    private final Long upStationId;
    private final Integer distance;

    public PathEntity(final Long stationId, final String name, final Long upStationId, final Integer distance) {
        this.stationId = stationId;
        this.name = name;
        this.upStationId = upStationId;
        this.distance = distance;
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

    public Integer getDistance() {
        return distance;
    }
}
