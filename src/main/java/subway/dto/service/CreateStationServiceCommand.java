package subway.dto.service;

public class CreateStationServiceCommand {
    private final String name;

    public CreateStationServiceCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
