package subway.service;

import subway.domain.Path;
import subway.domain.Station;

public interface PathFinderService {
    Path findPath(Station departure, Station arrival);
}
