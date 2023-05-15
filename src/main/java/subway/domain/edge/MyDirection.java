package subway.domain.edge;

import subway.domain.station.Station;

import java.util.List;
import java.util.Optional;

public interface MyDirection {

     Edges calculate(final Optional<Edge> existEdgeOptional, final List<Edge> edges,
                     final Station existStation, final Station newStation, final Integer distance);

     boolean isUp();

     boolean isDown();
}
