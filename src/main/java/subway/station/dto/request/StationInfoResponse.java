package subway.station.dto.request;

public class StationInfoResponse {

    private final long id;
    private final String name;

    public StationInfoResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
