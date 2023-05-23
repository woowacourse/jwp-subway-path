package subway.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {

    @NotNull
    private final String source;
    @NotNull
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
