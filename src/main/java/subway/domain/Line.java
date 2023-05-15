package subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Line {

    private Long id;
    private String name;
    private List<Edge> edges;

    public Line() {
    }

    public Line(Long id, String name, List<Edge> edges) {
        this.id = id;
        this.name = name;
        this.edges = edges;
    }

    public static Line createLine(String name, Station upStation, Station downStation, int distance) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(upStation, downStation, distance));
        return new Line(null, name, edges);
    }

    public void addEdge(Station upStation, Station downStation, int distance) {
        validateStations(upStation, downStation);
        Direction direction = findDirection(upStation);
        direction.add(edges, upStation, downStation, distance);
    }

    private void validateStations(Station upStation, Station downStation) {
        if (isAlreadyExistBoth(upStation, downStation)) {
            throw new IllegalArgumentException("해당 노선에 두 역이 모두 존재합니다.");
        }
        if (isNothingExist(upStation, downStation)) {
            throw new IllegalArgumentException("해당 노선에 두 역이 모두 존재하지 않습니다.");
        }
    }

    private Direction findDirection(Station upStation) {
        if (getStations().stream().anyMatch(station -> station.equals(upStation))) {
            return Direction.DOWN;
        }
        return Direction.UP;
    }

    private boolean isAlreadyExistBoth(Station upStation, Station downStation) {
        return new HashSet<>(getStations()).containsAll(List.of(upStation, downStation));
    }

    private boolean isNothingExist(Station upStation, Station downStation) {
        return !getStations().contains(upStation) && !getStations().contains(downStation);
    }

    public void deleteStation(Station station) {
        List<Edge> targetEdges = edges.stream()
                .filter(edge -> edge.hasStation(station))
                .collect(Collectors.toList());
        validateTargetEdges(targetEdges);

        if (targetEdges.size() == 1) {
            edges.remove(targetEdges.get(0));
        }
        if (targetEdges.size() == 2) {
            Edge edge1 = targetEdges.get(0);
            Edge edge2 = targetEdges.get(1);

            Edge newEdge = new Edge(edge1.getUpStation(), edge2.getDownStation(),
                    edge1.getDistance() + edge2.getDistance());
            int removedIndex = edges.indexOf(edge1);
            edges.remove(edge1);
            edges.remove(edge2);
            edges.add(removedIndex, newEdge);
        }
    }

    private static void validateTargetEdges(List<Edge> targetEdges) {
        if (targetEdges.isEmpty()) {
            throw new IllegalArgumentException("해당 역이 해당 노선에 존재하지 않습니다.");
        }
        if (targetEdges.size() > 2) {
            throw new IllegalArgumentException("해당 노선에 갈래길이 존재합니다. 확인해주세요.");
        }
    }

    public List<Station> getStations() {
        Map<Station, Station> stationToStation = edges.stream()
                .collect(Collectors.toMap(Edge::getUpStation, Edge::getDownStation));
        Set<Station> ups = new HashSet<>(stationToStation.keySet());
        ups.removeAll(stationToStation.values());

        List<Station> result = new ArrayList<>(ups);
        Station targetStation = result.get(0);
        while (stationToStation.containsKey(targetStation)) {
            Station next = stationToStation.get(targetStation);
            result.add(next);
            targetStation = next;
        }
        return result;
    }

    public List<Long> getStationIds() {
        return getStations().stream()
                .map(Station::getId)
                .collect(Collectors.toList());
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void setName(String name) {
        this.name = name;
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
