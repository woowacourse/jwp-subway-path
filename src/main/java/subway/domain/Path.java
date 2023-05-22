package subway.domain;

import java.util.List;

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
