package subway.station.dto.response;

import java.util.List;
import subway.station.dto.request.StationInfoResponse;

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
