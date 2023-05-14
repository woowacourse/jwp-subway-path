package subway.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class LinesResponse {

    @Schema(description = "노선 목록")
    private List<LineResponse> lines;

    public LinesResponse(final List<LineResponse> lines) {
        this.lines = lines;
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
