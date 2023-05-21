package subway.dto.line;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Line;

public class LinesSelectResponse {
    private List<LineSelectResponse> lines;

    private LinesSelectResponse() {
    }

    private LinesSelectResponse(final List<LineSelectResponse> lines) {
        this.lines = lines;
    }

    public static LinesSelectResponse from(List<Line> lines) {
        final List<LineSelectResponse> lineSelectResponses = lines.stream()
                .map(line -> LineSelectResponse.from(line))
                .collect(Collectors.toUnmodifiableList());

        return new LinesSelectResponse(lineSelectResponses);
    }

    public List<LineSelectResponse> getLines() {
        return lines;
    }
}
