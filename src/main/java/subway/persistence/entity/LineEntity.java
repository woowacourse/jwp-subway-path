package subway.persistence.entity;

public class LineEntity {

    private final Long id;
    private final String name;

    public LineEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public LineEntity(final String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
