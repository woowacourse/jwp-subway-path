package subway.service.station.dto;

public class StationInsertRequest {
    private String name;

    public StationInsertRequest() {
    }

    public StationInsertRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
