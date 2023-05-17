package subway.dto;


public class StationRequest {

    private final String name;

    public StationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
