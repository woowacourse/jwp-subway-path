package subway.dto;

import subway.domain.Section;

public class SectionResponse {
    private String startStation;
    private String endStation;
    private int distance;

    public SectionResponse() {
    }

    public SectionResponse(String startStation, String endStation, int distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(section.getStartStation().getName(), section.getEndStation().getName(),
                section.getDistance());
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public int getDistance() {
        return distance;
    }
}
