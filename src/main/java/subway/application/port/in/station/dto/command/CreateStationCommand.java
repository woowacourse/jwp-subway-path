package subway.application.port.in.station.dto.command;

public class CreateStationCommand {

    private final String name;

    public CreateStationCommand(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
