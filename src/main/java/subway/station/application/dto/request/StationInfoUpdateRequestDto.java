package subway.station.application.dto.request;

public class StationInfoUpdateRequestDto {

    private final long id;
    private final String name;

    public StationInfoUpdateRequestDto(long id, String name) {
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
