package subway.domain.edge;

import subway.domain.station.Station;

import java.util.List;

public interface DirectionStrategy {

     Edges calculate(final List<Edge> edges, final Station existStation, final Station newStation, final Integer distance);
}
