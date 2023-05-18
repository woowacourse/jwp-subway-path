package subway.ui.dto.response;

import java.util.List;

public class ReadPathResponse {

    final List<ReadStationResponse> stationResponses;

    private ReadPathResponse(final List<ReadStationResponse> stationResponses) {
        this.stationResponses = stationResponses;
    }

    public static ReadPathResponse from(final List<ReadStationResponse> stationResponses) {
        return new ReadPathResponse(stationResponses);
    }

    public List<ReadStationResponse> getStationResponses() {
        return stationResponses;
    }
}
