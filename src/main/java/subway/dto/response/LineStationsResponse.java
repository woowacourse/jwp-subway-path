package subway.dto.response;

import java.util.List;

public class LineStationsResponse {

    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;

    public LineStationsResponse(LineResponse lineResponse, List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
