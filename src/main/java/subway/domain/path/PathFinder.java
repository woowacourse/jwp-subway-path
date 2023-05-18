package subway.domain.path;

import java.util.List;

import subway.domain.Sections;
import subway.domain.Station;

public interface PathFinder {
    List<Station> getPathVerticies();

    Sections getPathEdges();
}
