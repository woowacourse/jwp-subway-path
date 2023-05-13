package subway.domain;

import subway.entity.LineEntity;

public class Line {
    private final Long id;
    private final String name;
    private final String color;

    public Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Line(final String name, final String color) {
        this(null, name, color);
    }

    public static Line from(final LineEntity lineEntity) {
        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor());
    }

    public static Line of(final String name, final String color) {
        return new Line(null, name, color);
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

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                '}';
    }
}
