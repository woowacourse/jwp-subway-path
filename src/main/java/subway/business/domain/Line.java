package subway.business.domain;

public class Line {

    private final long id;
    private final String name;
    private final String color;

    public Line(final long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
