package subway.domain.service;

import subway.domain.Lines;
import subway.domain.Station;

public interface ShortestRouteService {

    Lines shortestRoute(final Lines lines, final Station start, final Station end);
}
