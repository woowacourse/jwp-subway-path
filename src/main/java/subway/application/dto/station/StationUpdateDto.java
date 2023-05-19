package subway.application.dto.station;

public class StationUpdateDto {
    private Long id;
    private String name;

    public StationUpdateDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
