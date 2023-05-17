package subway.dto;

public class LineCreateRequest {

    private LineRequest lineRequest;
    private StationRequest stationRequest;

    public LineCreateRequest() {
    }

    public LineCreateRequest(LineRequest lineRequest, StationRequest stationRequest) {
        this.lineRequest = lineRequest;
        this.stationRequest = stationRequest;
    }

    public LineRequest getLineRequest() {
        return lineRequest;
    }

    public StationRequest getStationRequest() {
        return stationRequest;
    }
}
