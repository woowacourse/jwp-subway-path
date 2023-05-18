package subway.dto;

import java.util.List;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int price;

    public PathResponse(final List<StationResponse> stations, final int price) {
        this.stations = stations;
        this.price = price;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getPrice() {
        return price;
    }
}
