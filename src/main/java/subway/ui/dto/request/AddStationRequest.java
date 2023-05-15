package subway.ui.dto.request;

public class AddStationRequest {
    private final String firstStation;
    private final String secondStation;
    private final String newStation;
    private final int distance;

    public AddStationRequest(
            final String firstStation,
            final String secondStation,
            final String newStation,
            final int distance
    ) {
        this.firstStation = firstStation;
        this.secondStation = secondStation;
        this.newStation = newStation;
        this.distance = distance;
    }

    public String getFirstStation() {
        return firstStation;
    }

    public String getSecondStation() {
        return secondStation;
    }

    public String getNewStation() {
        return newStation;
    }

    public int getDistance() {
        return distance;
    }
}
