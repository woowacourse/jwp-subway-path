package subway.dto;

public class StationSaveDto {
    private String name;

    public StationSaveDto() {
    }

    public StationSaveDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
