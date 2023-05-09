package subway.dto;

public class StationUpdateRequest {
    private String name;

    public StationUpdateRequest() {
    }

    public StationUpdateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
