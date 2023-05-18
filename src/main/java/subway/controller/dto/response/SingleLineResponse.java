package subway.controller.dto.response;

import subway.service.domain.SingleLine;

import java.util.List;
import java.util.stream.Collectors;

public class SingleLineResponse {

    private final LinePropertyResponse linePropertyResponse;
    private final List<StationResponse> stationResponses;

    private SingleLineResponse(LinePropertyResponse linePropertyResponse,
                               List<StationResponse> stationResponses) {
        this.linePropertyResponse = linePropertyResponse;
        this.stationResponses = stationResponses;
    }

    public static SingleLineResponse from(SingleLine singleLine) {
        return new SingleLineResponse(
                LinePropertyResponse.from(singleLine.getLineProperty()),
                singleLine.getStations()
                        .stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public LinePropertyResponse getLinePropertyResponse() {
        return linePropertyResponse;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
