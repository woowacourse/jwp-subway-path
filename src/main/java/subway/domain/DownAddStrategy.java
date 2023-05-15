package subway.domain;

import java.util.List;
import java.util.Optional;

public class DownAddStrategy implements AddStrategy{

    @Override
    public void activate(List<Edge> edges, Station upStation, Station downStation, int distance) {
        // 기존 역이 upStation이다
        Edge existingEdge = edges.stream()
                .filter(edge -> edge.getUpStation().equals(upStation))
                .findFirst()
                .orElse(null);

        if (existingEdge != null) {
            if (distance < existingEdge.getDistance()) {
                Edge newEdge1 = new Edge(upStation, downStation, distance);
                Edge newEdge2 = new Edge(downStation, existingEdge.getDownStation(), existingEdge.getDistance() - distance);
                int removedIndex = edges.indexOf(existingEdge);
                edges.remove(removedIndex);
                edges.add(removedIndex, newEdge2);
                edges.add(removedIndex, newEdge1);
            } else {
                throw new IllegalArgumentException("추가하려는 거리가 기존의 거리보다 깁니다.");
            }
        } else {
            edges.add(new Edge(upStation, downStation, distance));
        }
    }
}
