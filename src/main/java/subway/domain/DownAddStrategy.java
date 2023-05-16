package subway.domain;

import java.util.List;
import java.util.Optional;

public class DownAddStrategy implements AddStrategy{

    @Override
    public void activate(List<Edge> edges, Station upStation, Station downStation, int distance) {
        // 기존 역이 upStation이다
        final Optional<Edge> edge = findSectionByStationExistsAtDirection(edges, upStation, Direction.UP);
        if (edge.isPresent()) {
            final Edge existingEdge = edge.get();
            if (existingEdge.getDistance() < distance) {
                throw new IllegalArgumentException("추가하려는 거리가 기존의 거리보다 깁니다.");
            }
            edges.add(new Edge(downStation, existingEdge.getDownStation(), existingEdge.getDistance() - distance));
            edges.remove(existingEdge);
        }
        edges.add(new Edge(upStation, downStation, distance));
    }
}
