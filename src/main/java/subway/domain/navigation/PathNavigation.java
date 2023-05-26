package subway.domain.navigation;

import subway.domain.line.edge.StationEdge;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface PathNavigation {

    List<Long> findPath(final Long startStationId, final Long endStationId, final Collection<StationEdge> subwayGraph);
}
