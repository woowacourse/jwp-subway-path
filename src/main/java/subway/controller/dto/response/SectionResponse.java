package subway.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import subway.domain.section.PathSection;

public class SectionResponse {

    @Schema(description = "상행역 이름")
    private String upwardStationName;

    @Schema(description = "하행역 이름")
    private String downwardStationName;

    @Schema(description = "상행역과 하행역 사이의 거리")
    private int distance;

    public SectionResponse(final String upwardStationName, final String downwardStationName, final int distance) {
        this.upwardStationName = upwardStationName;
        this.downwardStationName = downwardStationName;
        this.distance = distance;
    }

    public static SectionResponse from(final PathSection pathSection) {
        return new SectionResponse(
                pathSection.getSource().getName(),
                pathSection.getTarget().getName(),
                pathSection.getDistance()
        );
    }

    public String getUpwardStationName() {
        return upwardStationName;
    }

    public String getDownwardStationName() {
        return downwardStationName;
    }

    public int getDistance() {
        return distance;
    }
}

