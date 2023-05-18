package subway.presentation.dto.response;


import java.util.List;

public class LineResponse {

    private final long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(final long id, final String name,
                        final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

}
