package subway.dto;

public class StationResponse {

    private final String name;

    public StationResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
