package subway.ui.dto.section;

import subway.domain.Section;

public class SectionResponse {
    private Long id;
    private String startStationName;
    private String endStationName;
    private int distance;

    public SectionResponse(Long id, String startStationName, String endStationName, int distance) {
        this.id = id;
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.distance = distance;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getStartStation().getName(),
                section.getEndStation().getName(),
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

    public Long getId() {
        return id;
    }
}
