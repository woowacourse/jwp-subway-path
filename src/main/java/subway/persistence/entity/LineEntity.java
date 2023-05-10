package subway.persistence.entity;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;

    private LineEntity(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineEntity of(final Long id, final String name, final String color) {
        return new LineEntity(id, name, color);
    }

    public static LineEntity of(final String name, final String color) {
        return new LineEntity(null, name, color);
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
