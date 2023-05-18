package subway.dto.response;

import subway.domain.Section;

public class SectionQueryResponse {

    private String upStationName;
    private String downStationName;
    private int distance;

    private SectionQueryResponse() {
    }

    public SectionQueryResponse(final String upStationName, final String downStationName, final int distance) {
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public static SectionQueryResponse from(final Section section) {
        return new SectionQueryResponse(
                section.getUp().getName(),
                section.getDown().getName(),
                section.getDistance());
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }
}
