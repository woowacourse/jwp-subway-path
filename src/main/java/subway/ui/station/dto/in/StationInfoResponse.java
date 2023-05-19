package subway.ui.station.dto.in;

public class StationInfoResponse {

    private long id;
    private String name;

    private StationInfoResponse() {
    }

    public StationInfoResponse(final long id, final String name) {
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
