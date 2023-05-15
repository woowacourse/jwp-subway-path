package subway.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionRequest {

    @NotBlank
    private final String prevStationName;
    @NotBlank
    private final String nextStationName;
    @NotNull
    private final Integer distance;

    public SectionRequest(final String prevStationName, final String nextStationName, final Integer distance) {
        this.prevStationName = prevStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    private SectionRequest() {
        this(null, null, null);
    }

    public String getPrevStationName() {
        return prevStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
