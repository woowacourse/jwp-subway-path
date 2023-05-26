package subway.controller.station.dto;

public class StationInsertWebRequest {
    private String name;

    public StationInsertWebRequest() {
    }

    public StationInsertWebRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
