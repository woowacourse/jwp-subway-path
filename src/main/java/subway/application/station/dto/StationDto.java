package subway.application.station.dto;

import subway.persistence.entity.StationEntity;

public class StationDto {

	private Long id;
	private String name;

	public StationDto(final StationEntity stationEntity) {
		this(stationEntity.getId(), stationEntity.getName());
	}

	public StationDto(final Long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
