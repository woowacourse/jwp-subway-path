package subway.event;

public class DeleteSectionEvent {

    private final Long lineId;
    private final Long stationId;

    public DeleteSectionEvent(final Long lineId, final Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
