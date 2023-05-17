package subway.dto;

import java.util.List;
import subway.domain.Line;
import subway.domain.Station;

public class LineRouteResponse {
    
    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;
    
    public LineRouteResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }
    
    public static LineRouteResponse of(final Line line, final List<Station> stations) {
        return new LineRouteResponse(LineResponse.of(line), StationResponse.listOf(stations));
    }
    
    public LineResponse getLineResponse() {
        return this.lineResponse;
    }
    
    public List<StationResponse> getStationResponses() {
        return this.stationResponses;
    }
}
