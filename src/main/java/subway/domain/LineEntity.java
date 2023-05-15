package subway.domain;

import java.util.Objects;

public class LineEntity {
    private Long id;
    private String name;
    private String color;

    private LineEntity(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineEntity of(String name, String color) {
        return new LineEntity(null, name, color);
    }

    public static LineEntity of(Long id, String name, String color) {
        return new LineEntity(id, name, color);
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

    public void updateInfo(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineEntity lineEntity = (LineEntity) o;
        return Objects.equals(id, lineEntity.id) && Objects.equals(name, lineEntity.name) && Objects.equals(color, lineEntity.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
