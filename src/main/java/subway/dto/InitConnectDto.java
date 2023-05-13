package subway.dto;

public class InitConnectDto {
    private final Long lineId;
    private final Long sourceStationId;
    private final Long targetStationId;
    private final int distance;

    public InitConnectDto(Long lineId, Long stationId, Long nextStationId, int distance) {
        this.lineId = lineId;
        this.sourceStationId = stationId;
        this.targetStationId = nextStationId;
        this.distance = distance;
    }

    public static InitConnectDto of(Long lineId, Long stationId, ConnectRequest request) {
        return new InitConnectDto(lineId, stationId, request.getNextStationId(), request.getDistance());
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public int getDistance() {
        return distance;
    }
}
