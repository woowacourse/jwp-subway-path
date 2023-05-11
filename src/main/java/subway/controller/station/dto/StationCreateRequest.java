package subway.controller.station.dto;

public class StationCreateRequest {
    private String name;

    public StationCreateRequest() {
    }

    public StationCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
