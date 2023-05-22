package subway.dto;

import java.util.List;
import subway.domain.Line;
import subway.domain.Station;

public class LineRouteResponse {
    
    private final LineResponse line;
    private final List<StationResponse> stations;
    
    public LineRouteResponse(final LineResponse line, final List<StationResponse> stations) {
        this.line = line;
        this.stations = stations;
    }
    
    public static LineRouteResponse of(final Line line, final List<Station> stations) {
        return new LineRouteResponse(LineResponse.of(line), StationResponse.listOf(stations));
    }
    
    public LineResponse getLine() {
        return this.line;
    }
    
    public List<StationResponse> getStations() {
        return this.stations;
    }
}
