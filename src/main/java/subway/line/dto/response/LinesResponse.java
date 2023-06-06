package subway.line.dto.response;

import java.util.List;

public class LinesResponse {

    private List<LineResponse> lines;

    private LinesResponse() {
    }

    public LinesResponse(List<LineResponse> lines) {
        this.lines = lines;
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
