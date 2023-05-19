package subway.ui.station.dto.in;

import java.util.List;

public class StationInfosResponse {

    private List<StationInfoResponse> stations;

    private StationInfosResponse() {
    }

    public StationInfosResponse(final List<StationInfoResponse> stations) {
        this.stations = stations;
    }

    public List<StationInfoResponse> getStations() {
        return stations;
    }
}
