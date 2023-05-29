package subway.domain.path;

import java.util.List;

import subway.domain.line.Section;
import subway.domain.line.Station;

public interface PathFinder {

	void initialize(List<Section> sections);

	Path findShortestPath(final Station departure, final Station arrival);

}
