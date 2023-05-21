package subway.dto;

import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int additionalFare;
    private List<PathResponse> paths;

    public LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final int additionalFare, final List<PathResponse> paths) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.paths = paths;
    }

    public static LineResponse from(final Line line) {
        final List<PathResponse> paths = line.getPaths().getOrdered().stream()
                .map(PathResponse::from)
                .collect(Collectors.toUnmodifiableList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(), paths);
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

    public int getAdditionalFare() {
        return additionalFare;
    }

    public List<PathResponse> getPaths() {
        return paths;
    }
}
