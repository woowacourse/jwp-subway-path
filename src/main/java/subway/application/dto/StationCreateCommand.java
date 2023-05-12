package subway.application.dto;

import subway.domain.Station;

public class StationCreateCommand {

    private final String name;

    public StationCreateCommand(final String name) {
        this.name = name;
    }

    public Station toDomain() {
        return new Station(name);
    }

    public String name() {
        return name;
    }
}
