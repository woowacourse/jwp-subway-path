package subway.dto;

public class PathRequest {

    private final String source;
    private final String target;

    public PathRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
