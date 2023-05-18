package subway.dto;

import subway.domain.line.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineAndStationsResponse {

    private LineResponse lineResponse;
    private List<StationResponse> stationResponses;

    public LineAndStationsResponse() {
    }

    public LineAndStationsResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }

    public static LineAndStationsResponse of(final Line line) {
        final List<StationResponse> stationResponses = line.getOrderedStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
        return new LineAndStationsResponse(LineResponse.of(line), stationResponses);
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
