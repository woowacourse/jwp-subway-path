package subway.dto;

public class StationResponse {
    private final String name;

    public StationResponse(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
