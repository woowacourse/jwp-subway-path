package subway.domain.navigation;

import subway.domain.line.edge.StationEdge;
import subway.domain.line.edge.StationEdges;

import java.util.Set;

public interface PathNavigation {

    StationEdges findPath(final Long startStationId, final Long endStationId, final Set<StationEdge> subwayGraph);
}
