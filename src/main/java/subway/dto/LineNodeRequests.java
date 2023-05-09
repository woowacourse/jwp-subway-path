package subway.dto;

import java.util.List;

public class LineNodeRequests {

    private final String name;
    private final List<LineNodeRequest> requests;

    private LineNodeRequests() {
        this(null, null);
    }

    public LineNodeRequests(final String name, final List<LineNodeRequest> requests) {
        this.name = name;
        this.requests = requests;
    }

    public String getName() {
        return name;
    }

    public List<LineNodeRequest> getRequests() {
        return requests;
    }
}
