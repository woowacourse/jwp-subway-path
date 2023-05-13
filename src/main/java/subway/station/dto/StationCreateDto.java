package subway.station.dto;

public class StationCreateDto {

    private final String name;

    public StationCreateDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
