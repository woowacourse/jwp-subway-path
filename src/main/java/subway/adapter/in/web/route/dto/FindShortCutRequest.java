package subway.adapter.in.web.route.dto;

public class FindShortCutRequest {
    private String fromStation;
    private String toStation;

    public FindShortCutRequest() {
    }

    public FindShortCutRequest(final String fromStation, final String toStation) {
        this.fromStation = fromStation;
        this.toStation = toStation;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }
}
