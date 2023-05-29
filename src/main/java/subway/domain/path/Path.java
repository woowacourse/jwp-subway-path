package subway.domain.path;

import java.util.List;

import subway.domain.line.Distance;
import subway.domain.line.Station;

public class Path {

	private final List<Station> Path;
	private final Distance distance;

	public Path(final List<Station> path, final Distance distance) {
		Path = path;
		this.distance = distance;
	}

	public List<Station> getPath() {
		return Path;
	}

	public Distance getDistance() {
		return distance;
	}
}
