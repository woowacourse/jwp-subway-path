package subway.application.dto;

import subway.domain.line.Line;

public class CreationLineDto {

    private final Long id;
    private final String name;
    private final String color;

    private CreationLineDto(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static CreationLineDto from(final Line line) {
        return new CreationLineDto(line.getId(), line.getName(), line.getColor());
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
