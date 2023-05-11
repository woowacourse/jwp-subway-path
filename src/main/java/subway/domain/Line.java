package subway.domain;

public class Line {
    private final Long id;
    private final String name;

    public Line(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Line(final String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                '}';
    }
}
