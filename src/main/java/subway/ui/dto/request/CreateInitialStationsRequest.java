package subway.ui.dto.request;

public class CreateInitialStationsRequest {
    private final String firstStation;
    private final String secondStation;
    private final Integer distance;

    public CreateInitialStationsRequest(final String firstStation, final String secondStation, final Integer distance) {
        this.firstStation = firstStation;
        this.secondStation = secondStation;
        this.distance = distance;
    }

    public String getFirstStation() {
        return firstStation;
    }

    public String getSecondStation() {
        return secondStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
