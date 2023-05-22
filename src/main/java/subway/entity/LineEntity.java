package subway.entity;

import java.util.Objects;

public class LineEntity {

    private final Long id;
    private final String name;
    private final String color;
    private final Integer cost;

    public LineEntity(String name, String color) {
        this(null, name, color, 0);
    }

    public LineEntity(Long id, String name, String color) {
        this(id, name, color, 0);
    }

    public LineEntity(Long id, String name, String color, Integer cost) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.cost = cost;
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

    public Integer getCost() {
        return cost;
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
        return Objects.equals(id, lineEntity.id) && Objects.equals(name, lineEntity.name) && Objects.equals(color,
                lineEntity.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
