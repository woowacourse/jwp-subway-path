package subway.line.ui.dto.response;

import java.util.List;

public class LinesResponse {

    private final List<LineResponse> lines;

    private LinesResponse() {
        this(null);
    }

    public LinesResponse(List<LineResponse> lines) {
        this.lines = lines;
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
