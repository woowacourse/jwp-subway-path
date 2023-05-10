package subway.dto;

import subway.domain.Line;

public class ReadLineResponse {
    private final Long id;
    private final String name;
    private final String color;

    public ReadLineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static ReadLineResponse of(final Line line) {
        return new ReadLineResponse(line.getId(), line.getName(), line.getColor());
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
}
