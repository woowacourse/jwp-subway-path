package subway.controller.dto;

import java.util.List;

public class LinesResponse {

    private List<LineResponse> lines;

    public LinesResponse(final List<LineResponse> lines) {
        this.lines = lines;
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
