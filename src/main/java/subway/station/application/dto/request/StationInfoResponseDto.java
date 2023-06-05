package subway.station.application.dto.request;

public class StationInfoResponseDto {

    private final long id;
    private final String name;

    public StationInfoResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
