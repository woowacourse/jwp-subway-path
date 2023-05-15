package subway.domain.service;

import subway.domain.Path;
import subway.domain.Station;

public interface ShortestRouteService {

    Path shortestRoute(final Path path, final Station start, final Station end);
}
