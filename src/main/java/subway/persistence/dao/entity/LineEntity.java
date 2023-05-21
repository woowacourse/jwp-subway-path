package subway.persistence.dao.entity;

public class LineEntity {
    private final Long id;
    private final String name;
    private final String color;

    public LineEntity(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public LineEntity(String name, String color) {
        this(null, name, color);
    }

    public long getId() {
        if (id == null) {
            throw new IllegalStateException("현재 id값이 존재 하지않습니다.");
        }
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
