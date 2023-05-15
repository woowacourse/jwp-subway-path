package subway.path.domain;

import subway.line.domain.Station;

public interface ShortestRouteService {

    Path shortestRoute(final Path path, final Station start, final Station end);
}
