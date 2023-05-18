package subway.dto;

import subway.domain.Section;

public class SectionResponse {
    private final Long line;
    private final String from;
    private final String to;
    private final int distance;

    public SectionResponse(final Long line, final String from, final String to, final int distance) {
        this.line = line;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public static SectionResponse from(final Section section) {
        return new SectionResponse(section.getLindId(),
                section.getUpper().getName(),
                section.getLower().getName(),
                section.getDistance().getValue());
    }

    public Long getLine() {
        return line;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getDistance() {
        return distance;
    }
}
