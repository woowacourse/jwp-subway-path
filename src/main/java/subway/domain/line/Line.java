package subway.domain.line;

public class Line {

    private final Long id;
    private final LineName name;

    public Line(Long id, String name) {
        this.id = id;
        this.name = new LineName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getLineName();
    }

    @Override
    public String toString() {
        return "LineEntity{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
