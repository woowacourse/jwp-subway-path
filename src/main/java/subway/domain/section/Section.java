package subway.domain.section;

public class Section {

    private final Long lineId;
    private final Long upStationId;
    private final Long downStationId;
    private final Distance distance;

    public Section(final Long lineId, final Long upStationId, final Long downStationId, final Distance distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance.getDistance();
    }
}
