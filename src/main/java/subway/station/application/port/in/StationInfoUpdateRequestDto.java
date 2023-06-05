package subway.station.application.port.in;

public class StationInfoUpdateRequestDto {

    private final long id;
    private final String name;

    public StationInfoUpdateRequestDto(final long id, final String name) {
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
