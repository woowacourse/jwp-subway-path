package subway.dto;

public class PathRequest {

    private final Long from;
    private final Long to;

    public PathRequest(Long from, Long to) {
        this.from = from;
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }
}
