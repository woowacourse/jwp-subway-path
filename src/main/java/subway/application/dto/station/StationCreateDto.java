package subway.application.dto.station;

public class StationCreateDto {
    private String name;

    public StationCreateDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
