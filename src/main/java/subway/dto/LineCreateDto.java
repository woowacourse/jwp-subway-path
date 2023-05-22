package subway.dto;

public class LineCreateDto {
    private String lineName;
    private int extraCharge;
    private long upStationId;
    private long downStationId;
    private int distance;

    public LineCreateDto(String lineName, int extraCharge, long upStationId, long downStationId,
                          int distance) {
        this.lineName = lineName;
        this.extraCharge = extraCharge;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public int getExtraCharge() {
        return extraCharge;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
