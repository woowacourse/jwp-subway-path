package subway.dto.station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class StationSaveRequest {

    @NotBlank
    private final String sourceStation;
    @NotBlank
    private final String targetStation;
    @Positive
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
