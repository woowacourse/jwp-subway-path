package subway.domain.line;

import subway.domain.edge.Edge;

import java.util.List;

public class Line {

    private Long id;
    private String name;
    private List<Edge> edges;

    private Line() {
    }

    public Line(final String name) {
        this.name = name;
    }

    public Line(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", edges=" + edges +
                '}';
    }
}