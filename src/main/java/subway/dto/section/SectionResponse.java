package subway.dto.section;

import subway.domain.section.Section;

public class SectionResponse {
    private String startStationName;
    private String endStationName;
    private int distance;

    public SectionResponse(String startStationName, String endStationName, int distance) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(section.getUpBoundStation().getName(), section.getDownBoundStation().getName(),
                section.getDistance());
    }

    public String getStartStationName() {
        return startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public int getDistance() {
        return distance;
    }
}
