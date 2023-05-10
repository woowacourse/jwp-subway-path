package subway.dto;

public class StationSaveRequest {

    private String from;
    private String to;
    private int distance;

    public StationSaveRequest() {
    }

    public StationSaveRequest(final String from, final String to, final int distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getDistance() {
        return distance;
    }
}
