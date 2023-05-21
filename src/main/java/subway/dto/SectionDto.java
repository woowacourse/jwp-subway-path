package subway.dto;

import subway.domain.core.Section;

public class SectionDto {

    private final String start;
    private final String end;
    private final int distance;

    public SectionDto(final String start, final String end, final int distance) {
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public static SectionDto from(final Section section) {
        return new SectionDto(section.getStartName(), section.getEndName(), section.getDistanceValue());
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public int getDistance() {
        return distance;
    }
}
