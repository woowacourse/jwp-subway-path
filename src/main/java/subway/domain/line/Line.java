package subway.domain.line;

import subway.domain.edge.DirectionStrategy;
import subway.domain.edge.Distance;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.station.Station;

import java.util.List;
import java.util.Objects;

public class Line {

    private Long id;
    private String name;
    private Edges edges;

    private Line() {
    }

    public Line(final String name) {
        this.name = name;
    }

    public Line(final String name, final Edges edges) {
        this.name = name;
        this.edges = edges;
    }

    public Line(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Line(final Long id, final String name, final Edges edges) {
        this.id = id;
        this.name = name;
        this.edges = edges;
    }

    public void addEdge(final Station existStation, final Station newStation, final DirectionStrategy directionStrategy, final Distance distance) {
        this.edges = edges.add(existStation, newStation, directionStrategy, distance);
    }

    public void delete(final Station station) {
        this.edges = edges.delete(station);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Edges getEdges() {
        return edges;
    }

    public List<Edge> edges() {
        return edges.getEdges();
    }

    public List<Station> stations() {
        return edges.getStations();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Line line = (Line) other;
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
