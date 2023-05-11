package subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SectionRequest {

    @NotBlank
    private String beforeStationName;
    @NotBlank
    private String nextStationName;
    @NotNull
    private Integer distance;

    public SectionRequest(final String beforeStationName, final String nextStationName, final Integer distance) {
        this.beforeStationName = beforeStationName;
        this.nextStationName = nextStationName;
        this.distance = distance;
    }

    private SectionRequest() {
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
