package subway.ui.dto.request;

public class AddStationRequest {
    //nullable
    private final String firstStation;
    //nullable
    private final String secondStation; // TODO firstStation && secondStation 모두 null은 Exception
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
