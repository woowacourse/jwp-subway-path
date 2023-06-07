package subway.station.dto.response;

public class StationInfoResponse {

    private final long id;
    private final String name;
    private final String title;

    public StationInfoResponse(long id, String title) {
        this.id = id;
        name = "stations/" + id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}
