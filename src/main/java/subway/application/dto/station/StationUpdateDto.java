package subway.application.dto.station;

public class StationUpdateDto {
    private long id;
    private String name;

    public StationUpdateDto(long id, String name) {
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
