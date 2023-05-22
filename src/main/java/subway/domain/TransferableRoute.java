package subway.domain;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import subway.domain.entity.Station;
import subway.domain.vo.Distance;

public class TransferableRoute {

    private final GraphPath<Station, LineClassifiableSectionEdge> path;

    public TransferableRoute(final GraphPath<Station, LineClassifiableSectionEdge> path) {
        this.path = path;
    }

    public Distance totalDistance() {
        return new Distance((int) path.getWeight());
    }

    // TODO 요금정책 전달받도록 변경
    public int totalFare() {
        return FareCalculator.calculate(totalDistance());
    }

    public List<Station> stations() {
        return new ArrayList<>(path.getVertexList());
    }
}
