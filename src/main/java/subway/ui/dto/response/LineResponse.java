package subway.ui.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineResponse {
    private Long id;
    private String name;

    public LineResponse(final Line line) {
        this(line.getId(), line.getName());
    }

    public LineResponse(final Long line) {
        this(line, null);
    }

    public static List<LineResponse> of(final List<Line> lines) {
        return lines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName()))
                .collect(Collectors.toList());
    }
}
