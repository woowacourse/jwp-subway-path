package subway.dto.response;

import subway.domain.subway.Station;

public class StationResponse {

	private final long id;
	private final String name;

	private StationResponse(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public static StationResponse from(final Station station) {
		return new StationResponse(station.getId(), station.getName());
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
