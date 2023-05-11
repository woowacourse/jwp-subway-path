package subway.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Line {

    private Long id;
    private String name;
    private List<Edge> edges;

    public Line() {
    }

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Station upStation, Station downStation, int distance) {
        /* TODO : validate
        1. from, to 둘 중 하나가 기존 노선에 등록되어 있는지 확인 (둘 다 있으면 안됨)
        2. from, to 둘 중 기존 노선에 등록되어 있는 애 찾기
        3. 기존 노선의 해당 station를 가지고 있는 edge들 가져오기
        4. 그중 타겟이 되는 edge 선정
        3. 새로운 애랑 기존 애 up, down 판별
            down-up : 뒤에 추가
            up-down : 앞에 추가
            up-up 이거나 down-down : 사이에 추가
                기존 edge의 dist보다 작아야 함
        */

        // 1번
        if (isAlreadyExistBoth(upStation, downStation)) {
            throw new IllegalArgumentException("해당 노선에 두 역이 모두 존재합니다.");
        }
        if (isNothingExist(upStation, downStation)) {
            throw new IllegalArgumentException("해당 노선에 두 역이 모두 존재하지 않습니다.");
        }

        // 2번
        Optional<Station> upStationOptional = getStations().stream()
                .filter(station -> station.equals(upStation))
                .findFirst();
        Optional<Station> downStationOptional = getStations().stream()
                .filter(station -> station.equals(downStation))
                .findFirst();

        if (upStationOptional.isPresent()) {
            // 기존 역이 upStation이다
            Optional<Edge> edgeOptional = edges.stream()
                    .filter(edge -> edge.getUpStation().equals(upStationOptional.get()))
                    .findFirst();

            if (edgeOptional.isPresent()) {
                // 거리비교하면 됨
                Edge targetEdge = edgeOptional.get();
                if (distance < targetEdge.getDistance()) { // 성공적으로 추가 가능
                    Edge newEdge1 = new Edge(upStation, downStation, distance);
                    Edge newEdge2 = new Edge(downStation, targetEdge.getDownStation(),
                            targetEdge.getDistance() - distance);
                    int removedIndex = edges.indexOf(targetEdge);
                    edges.remove(removedIndex);
                    edges.add(removedIndex, newEdge2);
                    edges.add(removedIndex, newEdge1);
                } else {
                    throw new IllegalArgumentException("추가하려는 거리가 기존의 거리보다 깁니다.");
                }

            } else {
                // 종점이니까 그냥 추가하면 돼
                edges.add(new Edge(upStation, downStation, distance));
            }
        } else {
            // 기존 역이 downStation이다.
            Optional<Edge> edgeOptional = edges.stream()
                    .filter(edge -> edge.getDownStation().equals(downStationOptional.get()))
                    .findFirst();

            if (edgeOptional.isPresent()) {
                // 거리비교하면 됨
                Edge targetEdge = edgeOptional.get();
                if (distance < targetEdge.getDistance()) {
                    Edge edge1 = new Edge(targetEdge.getUpStation(), upStation, targetEdge.getDistance() - distance);
                    Edge edge2 = new Edge(upStation, downStation, distance);
                    int removedIndex = edges.indexOf(targetEdge);
                    edges.remove(removedIndex);
                    edges.add(removedIndex, edge2);
                    edges.add(removedIndex, edge1);
                } else {
                    throw new IllegalArgumentException("추가하려는 거리가 기존의 거리보다 깁니다.");
                }
            } else {
                // 종점이니까 그냥 추가하면 돼
                edges.add(0, new Edge(upStation, downStation, distance));
            }
        }
    }

    private boolean isNothingExist(Station upStation, Station downStation) {
        return !getStations().contains(upStation) && !getStations().contains(downStation);
    }

    private boolean isAlreadyExistBoth(Station upStation, Station downStation) {
        return getStations().containsAll(List.of(upStation, downStation));
    }

    public void deleteStation(Station station) {
        /*
        1. station이 그 line에 있는지 확인
        2. station을 포함하고 있는 edge들을 가져온다.
            - 2개일 경우 : 합치고 삭제
            - 1개일 경우 : 삭제
         */
        List<Edge> findEdges = edges.stream()
                .filter(edge -> edge.hasStation(station))
                .collect(Collectors.toList());

        if (findEdges.isEmpty()) {
            throw new IllegalArgumentException("해당 역이 해당 노선에 존재하지 않습니다.");
        }
        if (findEdges.size() > 2) {
            throw new IllegalArgumentException("해당 노선에 갈래길이 존재합니다. 확인해주세요.");
        }

        if (findEdges.size() == 1) {
            edges.remove(findEdges.get(0));
        }
        if (findEdges.size() == 2) {
            Edge edge1 = findEdges.get(0);
            Edge edge2 = findEdges.get(1);

            Edge newEdge = new Edge(edge1.getUpStation(), edge2.getDownStation(),
                    edge1.getDistance() + edge2.getDistance());
            int removedIndex = edges.indexOf(edge1);
            edges.remove(edge1);
            edges.remove(edge2);
            edges.add(removedIndex, newEdge);
        }
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
