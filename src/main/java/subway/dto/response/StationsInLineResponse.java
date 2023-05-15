package subway.dto.response;

public class StationsInLineResponse {
    private LineResponse lineResponse;
    private StationsResponse stationsResponse;

    public StationsInLineResponse() {
    }

    public StationsInLineResponse(LineResponse lineResponse, StationsResponse stationsResponse) {
        this.lineResponse = lineResponse;
        this.stationsResponse = stationsResponse;
    }

    public LineResponse getLineResponse() {
        return lineResponse;
    }

    public StationsResponse getStationsResponse() {
        return stationsResponse;
    }
}
