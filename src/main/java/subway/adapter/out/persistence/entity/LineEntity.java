package subway.adapter.out.persistence.entity;

import java.util.Objects;

public class LineEntity {

    private Long id;
    private String name;
    private String color;
    private int surcharge;

    public LineEntity() {
    }

    public LineEntity(String name, String color, int surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public LineEntity(Long id, String name, String color, int surcharge) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getSurcharge() {
        return surcharge;
    }
}
