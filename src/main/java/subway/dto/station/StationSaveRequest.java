package subway.dto.station;

public class StationSaveRequest {

    private final String sourceStation;
    private final String targetStation;
    private final int distance;

    public StationSaveRequest(String sourceStation, String targetStation, int distance) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.distance = distance;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }

    public int getDistance() {
        return distance;
    }
}
