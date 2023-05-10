package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Line {

    private Long id;
    private final String name;
    private List<Edge> edges;

    private Line(String name, List<Edge> edges) {
        this.name = name;
        this.edges = edges;
    }

    public Line(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Line(Long id, String name, List<Edge> edges) {
        this.id = id;
        this.name = name;
        this.edges = edges;
    }

    public static Line createLine(String name, Station from, Station to, int distance) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(from, to, distance));
        return new Line(name, edges);
    }

    public List<Station> getStations() {
        List<Station> result = edges.stream()
                .map(Edge::getUpStation)
                .collect(Collectors.toList());
        result.add(edges.get(edges.size() - 1).getDownStation());
        return result;
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

    public void addEdge(Station from, Station to, int distance) {
        /* TODO : validate
        1. from, to 둘 중 하나는 포함되어 있어야 한다.
        2. from, to 둘 다 포함되어서는 안된다.
        3. from 또는 to가 존재하고, 이미 존재하는 edge의 distance와 동일하면 안된다.
        4. 기존의 edge와 방향이 같을 경우 길이는 기존 dist보다 짧아야 한다.
        -> validate 실패시 예외 발생
         */
        edges.add(new Edge(from, to, distance));
    }

    public void deleteEdge(Station station) {

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
