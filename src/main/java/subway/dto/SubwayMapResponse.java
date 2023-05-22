package subway.dto;

import subway.domain.Station;
import subway.entity.LineEntity;

import java.util.List;

public class SubwayMapResponse {

    private final LineResponse lineResponse;
    private final List<StationResponse> stationResponses;

    public SubwayMapResponse(final LineResponse lineResponse, final List<StationResponse> stationResponses) {
        this.lineResponse = lineResponse;
        this.stationResponses = stationResponses;
    }

    public static SubwayMapResponse of(final LineEntity lineEntity, final List<Station> stations) {
        return new SubwayMapResponse(LineResponse.of(lineEntity), StationResponse.listOf(stations));
    }

    public LineResponse getLineResponse() {
        return this.lineResponse;
    }

    public List<StationResponse> getStationResponses() {
        return this.stationResponses;
    }
}
