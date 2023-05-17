package subway.dto;

import java.util.List;

public class LineStationResponse {

    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;

    private LineStationResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }

    public static LineStationResponse from(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        return new LineStationResponse(lineResponse, stationResponses);
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
