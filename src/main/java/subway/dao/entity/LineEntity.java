package subway.dao.entity;

public class LineEntity {
    private final Long id;
    private final String name;

    public LineEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public LineEntity(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
