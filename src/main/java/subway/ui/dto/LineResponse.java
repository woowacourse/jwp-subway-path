package subway.ui.dto;

import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;

    public LineResponse() {
    }

    public LineResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static List<LineResponse> of(final List<Line> lines) {
        return lines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName()))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
