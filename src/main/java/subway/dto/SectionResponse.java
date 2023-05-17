package subway.dto;

public class SectionResponse {
    private final Long line;
    private final Long from;
    private final Long to;
    private final int distance;

    public SectionResponse(final Long line, final Long from, final Long to, final int distance) {
        this.line = line;
        this.from = from;
        this.to = to;
        this.distance = distance;
    }

    public Long getLine() {
        return line;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public int getDistance() {
        return distance;
    }
}
