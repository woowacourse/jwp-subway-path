package subway.ui.dto.response;

import java.util.List;

public class LineResponse {

    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(final String name, final String color, final List<StationResponse> stations) {
        this.name = name;
        this.color = color;
        this.stations = stations;
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
