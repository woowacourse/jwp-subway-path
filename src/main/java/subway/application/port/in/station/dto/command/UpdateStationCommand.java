package subway.application.port.in.station.dto.command;

public class UpdateStationCommand {

    private final long stationId;
    private final String name;

    public UpdateStationCommand(final long stationId, final String name) {
        this.stationId = stationId;
        this.name = name;
    }

    public long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }
}
