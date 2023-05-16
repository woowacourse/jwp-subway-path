package subway.dto.response;

import subway.dto.PathAndFee;

public class PathAndFeeResponse {

    private StationsResponse stationsResponse;
    private int fee;

    public PathAndFeeResponse() {
    }

    public PathAndFeeResponse(StationsResponse stationsResponse, int fee) {
        this.stationsResponse = stationsResponse;
        this.fee = fee;
    }

    public static PathAndFeeResponse of(PathAndFee pathAndFee) {
        return new PathAndFeeResponse(new StationsResponse(pathAndFee.getStations()), pathAndFee.getFee());
    }

    public StationsResponse getStationsResponse() {
        return stationsResponse;
    }

    public int getFee() {
        return fee;
    }
}
