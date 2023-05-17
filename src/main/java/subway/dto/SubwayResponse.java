package subway.dto;

import java.util.List;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

public class SubwayResponse {
    
    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;
    
    public SubwayResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }
    
    public static SubwayResponse of(final LineEntity lineEntity, final List<StationEntity> stationEntities) {
        return new SubwayResponse(LineResponse.of(lineEntity), StationResponse.listOf(stationEntities));
    }
    
    public LineResponse getLineResponse() {
        return this.lineResponse;
    }
    
    public List<StationResponse> getStationResponses() {
        return this.stationResponses;
    }
}
