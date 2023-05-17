package subway.ui.dto.response;

import subway.domain.Station;

import java.util.List;

public class CreationPathResponse {

    final List<ReadStationResponse> stationResponses;

    private CreationPathResponse(final List<ReadStationResponse> stationResponses) {
        this.stationResponses = stationResponses;
    }

    public static CreationPathResponse from(final List<ReadStationResponse> stationResponses) {
        return new CreationPathResponse(stationResponses);
    }

    public List<ReadStationResponse> getStationResponses() {
        return stationResponses;
    }
}
