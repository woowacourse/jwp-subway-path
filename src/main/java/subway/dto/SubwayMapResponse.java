package subway.dto;

import subway.entity.LineEntity;
import subway.entity.StationEntity;

import java.util.List;

public class SubwayMapResponse {
    
    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;
    
    public SubwayMapResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }

    public static SubwayMapResponse of(final LineEntity lineEntity, final List<StationEntity> stationEntities) {
        return new SubwayMapResponse(LineResponse.of(lineEntity), StationResponse.listOf(stationEntities));
    }
    
    public LineResponse getLineResponse() {
        return this.lineResponse;
    }
    
    public List<StationResponse> getStationResponses() {
        return this.stationResponses;
    }
}
