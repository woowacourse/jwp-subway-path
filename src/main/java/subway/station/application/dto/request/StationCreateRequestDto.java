package subway.station.application.dto.request;

public class StationCreateRequestDto {

    private final String name;

    public StationCreateRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
