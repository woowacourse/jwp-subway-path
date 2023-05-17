package subway.application.port.in.route.dto.command;

public class FindRouteCommand {

    private final long sourceStationId;
    private final long targetStationId;

    public FindRouteCommand(final long sourceStationId, final long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }
}
