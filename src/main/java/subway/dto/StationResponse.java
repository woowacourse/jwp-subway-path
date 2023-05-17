package subway.dto;

public class StationResponse {
    private String name;

    private StationResponse() {
    }

    public StationResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
