package subway.application.port.in.section.dto.command;

public class RemoveStationFromLineCommand {

    private final long lineId;
    private final long stationId;

    public RemoveStationFromLineCommand(final long lineId, final long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public long getLineId() {
        return lineId;
    }

    public long getStationId() {
        return stationId;
    }
}
