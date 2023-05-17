package subway.ui.dto.response;

import subway.domain.Line;

public class CreationLineResponse {

    private final Long id;
    private final String name;
    private final String color;

    private CreationLineResponse(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static CreationLineResponse from(final Line line) {
        return new CreationLineResponse(line.getId(), line.getName(), line.getColor());
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
