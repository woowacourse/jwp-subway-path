package subway.dto;

import subway.domain.Line;
import subway.domain.Paths;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<PathResponse> paths;

    public LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final List<PathResponse> paths) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.paths = paths;
    }

    public LineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor());
    }

    public static LineResponse of(Line line, Paths paths) {
        final List<PathResponse> pathResponses = paths.getOrderedPaths().stream()
                .map(PathResponse::from)
                .collect(Collectors.toUnmodifiableList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), pathResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<PathResponse> getPaths() {
        return paths;
    }
}
