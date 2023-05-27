package subway.ui.dto.request;

public class PathRequest {

    private String fromStationName;
    private String toStationName;

    private PathRequest() {
    }

    public PathRequest(final String fromStationName, final String toStationName) {
        this.fromStationName = fromStationName;
        this.toStationName = toStationName;
    }

    public String getFromStationName() {
        return fromStationName;
    }

    public String getToStationName() {
        return toStationName;
    }
}
