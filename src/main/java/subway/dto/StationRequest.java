package subway.dto;

public class StationRequest {

    private final String name;

    private StationRequest() {
        this.name = null;
    }

    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
