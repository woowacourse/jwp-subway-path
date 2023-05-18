package subway.domain.path;

import subway.domain.Sections;
import subway.domain.Station;

public interface PathFinder {

    PathInfo findPath(Sections sections, Station source, Station target);
}
