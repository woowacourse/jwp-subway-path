package subway.application.port.in.station.dto.response;

public class StationQueryResponse {

    private long id;
    private String name;

    public StationQueryResponse() {
    }

    public StationQueryResponse(final long id, final String name) {
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
