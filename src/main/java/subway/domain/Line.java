package subway.domain;

public class Line {
    private Long id;
    private String name;

    public Line(final String name) {
        this.name = name;
    }

    public Line(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
