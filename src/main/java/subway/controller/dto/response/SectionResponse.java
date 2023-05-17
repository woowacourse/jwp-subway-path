package subway.controller.dto.response;

import subway.domain.section.PathSection;

public class SectionResponse {

    private String upwardStationName;
    private String downwardStationName;
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

