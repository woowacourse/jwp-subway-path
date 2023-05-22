package subway.domain;

import java.util.List;

public interface PathFinder {

	void initialize(List<Section> sections);

	Path findShortestPath(final Station departure, final Station arrival);

}
