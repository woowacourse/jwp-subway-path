package subway.application.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionRequest {

    @NotBlank
    private final String beforeStationName;
    @NotBlank
    private final String nextStationName;
    @NotNull
    private final Integer distance;

    public SectionRequest(final String beforeStationName, final String nextStationName, final Integer distance) {
        this.beforeStationName = beforeStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    private SectionRequest() {
        this(null, null, null);
    }

    public String getBeforeStationName() {
        return beforeStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
