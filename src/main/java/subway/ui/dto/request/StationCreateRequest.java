package subway.ui.dto.request;

public class StationCreateRequest {
    private String name;

    public StationCreateRequest() {
    }

    public StationCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
