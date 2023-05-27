package subway.dto.response;

import java.util.List;

public class StationsResponse {

	private final List<StationResponse> stations;

	private StationsResponse(final List<StationResponse> stations) {
		this.stations = stations;
	}

	public static StationsResponse from(final List<StationResponse> stations) {
		return new StationsResponse(stations);
	}

	public List<StationResponse> getStations() {
		return stations;
	}
}
