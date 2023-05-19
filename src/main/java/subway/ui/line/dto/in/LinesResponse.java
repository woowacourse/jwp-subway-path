package subway.ui.line.dto.in;

import java.util.List;

public class LinesResponse {

    private final List<LineResponse> lines;

    private LinesResponse() {
        this(null);
    }

    public LinesResponse(final List<LineResponse> lines) {
        this.lines = lines;
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
