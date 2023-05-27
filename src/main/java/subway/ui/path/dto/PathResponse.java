package subway.ui.path.dto;

import java.util.List;

import subway.ui.station.dto.StationResponse;

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
