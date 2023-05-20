package subway.dto.response;

import subway.domain.core.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
	private final long id;
	private final String name;

	public StationResponse(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public static List<StationResponse> of(List<Station> stations) {
		return stations.stream()
			.map(station -> new StationResponse(station.getId(), station.getName()))
			.collect(Collectors.toList());
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
