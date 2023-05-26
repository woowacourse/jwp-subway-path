package subway.entity;

public class SectionDetailEntity {

    private final Long id;
    private final Long lineId;
    private final String lineName;
    private final String lineColor;
    private final Long upStationId;
    private final String upStationName;
    private final Long downStationId;
    private final String downStationName;
    private final int distance;
    private final int order;

    public SectionDetailEntity(final Long id, final Long lineId, final String lineName, final String lineColor, final Long upStationId, final String upStationName, final Long downStationId, final String downStationName, final int distance, final int order) {
        this.id = id;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.upStationId = upStationId;
        this.upStationName = upStationName;
        this.downStationId = downStationId;
        this.downStationName = downStationName;
        this.distance = distance;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }

    public int getOrder() {
        return order;
    }
}
