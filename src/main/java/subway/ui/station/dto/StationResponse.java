package subway.ui.station.dto;

import subway.application.station.dto.StationDto;

public class StationResponse {
	private Long id;
	private String name;

	public StationResponse(final StationDto stationDto) {
		this(stationDto.getId(), stationDto.getName());
	}

	public StationResponse(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public static StationResponse from(final StationDto station) {
		return new StationResponse(station.getId(), station.getName());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
