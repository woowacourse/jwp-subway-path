package subway.ui.dto;

import subway.application.section.dto.SectionDto;

public class SectionResponse {

	private Long id;
	private String departure;
	private String arrival;
	private int distance;

	public SectionResponse(final Long id, final String departure, final String arrival, final int distance) {
		this.id = id;
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
	}

	public static SectionResponse from(final SectionDto sectionDto) {
		return new SectionResponse(sectionDto.getId(), sectionDto.getDeparture(), sectionDto.getArrival(),
			sectionDto.getDistance());
	}

	public Long getId() {
		return id;
	}

	public String getDeparture() {
		return departure;
	}

	public String getArrival() {
		return arrival;
	}

	public int getDistance() {
		return distance;
	}
}
