package subway.ui.dto;

import java.util.List;

public class PathResponse {

	private List<StationResponse> stations;
	private int fee;
	private int distance;

	public PathResponse() {
	}

	public PathResponse(final List<StationResponse> stations, final int fee, final int distance) {
		this.stations = stations;
		this.fee = fee;
		this.distance = distance;
	}

	public List<StationResponse> getStations() {
		return stations;
	}

	public int getFee() {
		return fee;
	}

	public int getDistance() {
		return distance;
	}
}
