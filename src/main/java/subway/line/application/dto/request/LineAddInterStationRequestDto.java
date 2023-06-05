package subway.line.application.dto.request;

public class LineAddInterStationRequestDto {

    private final Long id;
    private final Long upStationId;
    private final Long downStationId;
    private final Long newStationId;
    private final long distance;

    public LineAddInterStationRequestDto(Long id, Long upStationId, Long downStationId,
            Long newStationId,
            long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.newStationId = newStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getNewStationId() {
        return newStationId;
    }

    public long getDistance() {
        return distance;
    }
}
