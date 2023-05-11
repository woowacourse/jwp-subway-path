package subway.ui.dto;

import subway.dao.section.dto.SectionDto;

public class AddStationResponse {

	private Long sectionId;
	private String departureStation;
	private String arrivalStation;
	private int distance;

	public AddStationResponse(final SectionDto sectionDto) {
		this(sectionDto.getId(), sectionDto.getDeparture(), sectionDto.getArrival(), sectionDto.getDistance());
	}

	public AddStationResponse(final Long sectionId, final String departureStation, final String arrivalStation,
		final int distance) {
		this.sectionId = sectionId;
		this.departureStation = departureStation;
		this.arrivalStation = arrivalStation;
		this.distance = distance;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public String getDepartureStation() {
		return departureStation;
	}

	public String getArrivalStation() {
		return arrivalStation;
	}

	public int getDistance() {
		return distance;
	}
}
