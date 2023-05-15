package subway.domain;

import java.util.List;

public class UpAddStrategy implements AddStrategy {
    @Override
    public void activate(List<Edge> edges, Station upStation, Station downStation, int distance) {
        // 기존 역이 downStation이다.
        Edge existingEdge = edges.stream()
                .filter(edge -> edge.getDownStation().equals(downStation))
                .findFirst()
                .orElse(null);

        if (existingEdge != null) {
            if (distance < existingEdge.getDistance()) {
                Edge edge1 = new Edge(existingEdge.getUpStation(), upStation, existingEdge.getDistance() - distance);
                Edge edge2 = new Edge(upStation, downStation, distance);
                int removedIndex = edges.indexOf(existingEdge);
                edges.remove(removedIndex);
                edges.add(removedIndex, edge2);
                edges.add(removedIndex, edge1);
            } else {
                throw new IllegalArgumentException("추가하려는 거리가 기존의 거리보다 깁니다.");
            }
        } else {
            edges.add(0, new Edge(upStation, downStation, distance));
        }
    }
}
