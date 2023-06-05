package subway.line.application.dto.request;

public class LineCreateRequestDto {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final long distance;

    public LineCreateRequestDto(String name, String color, Long upStationId, Long downStationId,
            long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public long getDistance() {
        return distance;
    }
}
