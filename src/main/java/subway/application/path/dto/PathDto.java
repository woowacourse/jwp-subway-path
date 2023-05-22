package subway.application.path.dto;

import java.util.List;

import subway.application.station.dto.StationDto;

public class PathDto {

	private final List<StationDto> stations;
	private final int fee;
	private final int distance;

	public PathDto(final List<StationDto> stations, final int fee, final int distance) {
		this.stations = stations;
		this.fee = fee;
		this.distance = distance;
	}

	public List<StationDto> getStations() {
		return stations;
	}

	public int getFee() {
		return fee;
	}

	public int getDistance() {
		return distance;
	}
}
