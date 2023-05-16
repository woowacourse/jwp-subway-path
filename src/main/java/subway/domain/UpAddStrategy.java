package subway.domain;

import java.util.List;
import java.util.Optional;

public class UpAddStrategy implements AddStrategy {
    @Override
    public void activate(List<Edge> edges, Station upStation, Station downStation, int distance) {
        // 기존 역이 downStation이다.
        final Optional<Edge> edge = findSectionByStationExistsAtDirection(edges, downStation, Direction.DOWN);
        if (edge.isPresent()) {
            final Edge existingEdge = edge.get();
            if (existingEdge.getDistance() < distance) {
                throw new IllegalArgumentException("추가하려는 거리가 기존의 거리보다 깁니다.");
            }
            edges.add(new Edge(existingEdge.getUpStation(), upStation, existingEdge.getDistance() - distance));
            edges.remove(existingEdge);
        }
        edges.add(new Edge(upStation, downStation, distance));
    }
}
