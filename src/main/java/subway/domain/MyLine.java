package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyLine {

    private final String name;
    private final List<MyEdge> edges;

    private MyLine(String name, List<MyEdge> edges) {
        this.name = name;
        this.edges = edges;
    }

    public static MyLine createLine(String name, MyStation from, MyStation to, int distance) {
        List<MyEdge> edges = new ArrayList<>();
        edges.add(new MyEdge(from, to, distance));
        return new MyLine(name, edges);
    }

    public List<MyStation> getStations() {
        List<MyStation> result = edges.stream()
                .map(MyEdge::getFrom)
                .collect(Collectors.toList());
        result.add(edges.get(edges.size() - 1).getTo());
        return result;
    }

    public String getName() {
        return name;
    }

    public List<MyEdge> getEdges() {
        return edges;
    }

    public void addEdge(MyStation from, MyStation to, int distance) {
        /* TODO : validate
        1. from, to 둘 중 하나는 포함되어 있어야 한다.
        2. from, to 둘 다 포함되어서는 안된다.
        3. from 또는 to가 존재하고, 이미 존재하는 edge의 distance와 동일하면 안된다.
        4. 기존의 edge와 방향이 같을 경우 길이는 기존 dist보다 짧아야 한다.
        -> validate 실패시 예외 발생
         */
        edges.add(new MyEdge(from, to, distance));
    }


}
