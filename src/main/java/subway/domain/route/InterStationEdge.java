package subway.domain.route;

public class InterStationEdge {

    private final long upStationId;
    private final long downStationId;
    private final long weight;
    private final long lineId;

    public InterStationEdge(final long upStationId, final long downStationId, final long weight, final long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.weight = weight;
        this.lineId = lineId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getWeight() {
        return weight;
    }

    public long getLineId() {
        return lineId;
    }
}
