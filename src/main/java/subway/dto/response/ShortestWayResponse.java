package subway.dto.response;

import java.util.List;

public class ShortestWayResponse {

    private final int fee;
    private final List<StationResponse> stations;

    public ShortestWayResponse(final int fee, final List<StationResponse> stations) {
        this.fee = fee;
        this.stations = stations;
    }

    public int getFee() {
        return fee;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
