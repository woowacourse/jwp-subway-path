package subway.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(
        description = "노선 목록 응답 정보",
        example = "{\"lines\": [{\"id\": 1, \"name\": \"2호선\", \"color\": \"초록색\", \"fare\": 1000, \"stations\": [{\"id\": 1, \"name\": \"잠실역\"}]}]}"
)
public class LinesResponse {

    @Schema(description = "노선 목록")
    private List<LineResponse> lines;

    public LinesResponse() {
    }

    public LinesResponse(final List<LineResponse> lines) {
        this.lines = lines;
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
