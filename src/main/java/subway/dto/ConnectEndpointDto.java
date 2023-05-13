package subway.dto;

public class ConnectEndpointDto {
    private final Long lineId;
    private final Long stationId;
    private final int distance;

    public ConnectEndpointDto(Long lineId, Long stationId, int distance) {
        this.lineId = lineId;
        this.stationId = stationId;
        this.distance = distance;
    }

    public static ConnectEndpointDto of(Long lineId, Long stationId, ConnectRequest request) {
        return new ConnectEndpointDto(lineId, stationId, request.getDistance());
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }
}
