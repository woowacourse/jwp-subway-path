package subway.station.dto.response;

import subway.station.domain.Station;

public class StationInfoResponse {

    private final long id;
    private final String name;
    private final String title;

    public StationInfoResponse(long id, String title) {
        this.id = id;
        name = "stations/" + id;
        this.title = title;
    }

    public static StationInfoResponse from(Station station) {
        return new StationInfoResponse(station.getId(), station.getName().getValue());
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
