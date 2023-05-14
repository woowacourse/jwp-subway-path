package subway.application.port.in.section.dto.command;

public class AddStationToLineCommand {

    private final long lineId;
    private final long upStationId;
    private final long downStationId;
    private final int distance;

    public AddStationToLineCommand(final long lineId, final long upStationId, final long downStationId,
            final int distance) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public long getLineId() {
        return lineId;
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
