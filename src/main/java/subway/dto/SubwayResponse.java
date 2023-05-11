package subway.dto;

import java.util.List;
import subway.domain.Line;
import subway.domain.Station;

public class SubwayResponse {
    
    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;
    
    public SubwayResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }
    
    public static SubwayResponse of(final Line line, final List<Station> stations) {
        return new SubwayResponse(LineResponse.of(line), StationResponse.listOf(stations));
    }
    
    public LineResponse getLineResponse() {
        return this.lineResponse;
    }
    
    public List<StationResponse> getStationResponses() {
        return this.stationResponses;
    }
}
