package subway.station.dto.response;

public class StationInfoResponse {

    private long id;
    private String name;

    private StationInfoResponse() {
    }

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
