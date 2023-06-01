package subway.dto.response;

import java.util.List;

public class ShortestPathResponse {

    private int fee;
    private List<StationResponse> stations;

    public ShortestPathResponse() {
    }

    public ShortestPathResponse(final int fee, final List<StationResponse> stations) {
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
