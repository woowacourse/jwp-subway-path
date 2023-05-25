package subway.business.service.dto;

public class PathCalculateDto {
    private final long sourceStationId;
    private final long targetStationId;
    private final String passengerText;

    public PathCalculateDto(long sourceStationId, long targetStationId, String passengerText) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.passengerText = passengerText;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }

    public String getPassengerText() {
        return passengerText;
    }
}
