package subway.dto.line;

import subway.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

public class LinesResponse {

    private final List<LineResponse> lines;

    public LinesResponse(final List<LineResponse> lines) {
        this.lines = lines;
    }

    public static LinesResponse from(final List<LineEntity> lineEntities) {
        return lineEntities.stream()
                .map(LineResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), LinesResponse::new));
    }

    public List<LineResponse> getLines() {
        return lines;
    }
}
