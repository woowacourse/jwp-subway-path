package subway.dto;

import java.util.List;

public class FinalLineResponse {

    private final Long id;
    private final String name;
    private final List<StationResponse> stations;

    public FinalLineResponse(final Long id, final String name, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    private FinalLineResponse() {
        this(null, null, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
