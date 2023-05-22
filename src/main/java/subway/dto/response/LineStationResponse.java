package subway.dto.response;

import java.util.List;

public class LineStationResponse {

	private final List<StationResponse> stations;

	public LineStationResponse(final List<StationResponse> stations) {
		this.stations = stations;
	}

	public static LineStationResponse from(final List<StationResponse> stations) {
		return new LineStationResponse(stations);
	}

	public List<StationResponse> getStations() {
		return stations;
	}
}
