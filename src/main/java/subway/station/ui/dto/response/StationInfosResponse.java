package subway.station.ui.dto.response;

import java.util.List;

public class StationInfosResponse {

    private List<StationInfoResponse> stations;

    private StationInfosResponse() {
    }

    public StationInfosResponse(List<StationInfoResponse> stations) {
        this.stations = stations;
    }

    public List<StationInfoResponse> getStations() {
        return stations;
    }
}
