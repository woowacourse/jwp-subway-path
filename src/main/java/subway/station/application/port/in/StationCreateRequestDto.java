package subway.station.application.port.in;

public class StationCreateRequestDto {

    private final String name;

    public StationCreateRequestDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
