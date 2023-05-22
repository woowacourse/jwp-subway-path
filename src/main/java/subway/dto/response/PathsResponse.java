package subway.dto.response;

import subway.domain.subway.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PathsResponse {

	private final List<PathResponse> paths;

	public PathsResponse(final List<PathResponse> paths) {
		this.paths = paths;
	}

	public static PathsResponse from(final List<Station> stations, List<Set<String>> transferLines) {
		List<PathResponse> paths = new ArrayList<>();

		for (int i = 0; i < stations.size(); i++) {
			paths.add(PathResponse.from(stations.get(i), transferLines.get(i)));
		}

		return new PathsResponse(paths);
	}

	public List<PathResponse> getPaths() {
		return paths;
	}
}
