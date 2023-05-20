package subway.station.presentation.dto.request;

public class StationSaveRequest {

    private String name;

    public StationSaveRequest() {
    }

    public StationSaveRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
