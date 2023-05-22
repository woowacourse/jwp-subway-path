package subway.domain.path;

import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

public interface PathFinder {

    PathFinder registerSections(List<Sections> sections);

    double findDistance(Station source, Station target);

    List<Station> findPath(Station source, Station target);
}
