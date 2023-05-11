package subway.dto;

public class StationRequest {
    private String name;
    private String next;
    private int distance;

    public StationRequest() {
    }

    public StationRequest(String name, String next, int distance) {
        this.name = name;
        this.next = next;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public String getNext() {
        return next;
    }

    public int getDistance() {
        return distance;
    }
}
