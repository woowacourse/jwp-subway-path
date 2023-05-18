package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationsByLineResponse {

    private LineResponse lineResponse;
    private List<StationResponse> stationResponses;

    public StationsByLineResponse() {
    }

    public StationsByLineResponse(final Line line, final List<Station> stations) {
        lineResponse = new LineResponse(line);
        stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
