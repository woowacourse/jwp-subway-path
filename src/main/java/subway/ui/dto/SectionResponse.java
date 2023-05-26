package subway.ui.dto;

import subway.application.section.dto.SectionDto;
import subway.application.station.dto.StationDto;

public class SectionResponse {

	private Long id;
	private StationResponse departure;
	private StationResponse arrival;
	private int distance;

	public SectionResponse(final Long id, final StationResponse departure, final StationResponse arrival,
		final int distance) {
		this.id = id;
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
	}

	public static SectionResponse from(final SectionDto sectionDto) {
		final StationDto departure = sectionDto.getDeparture();
		final StationDto arrival = sectionDto.getArrival();

		return new SectionResponse(sectionDto.getId(), new StationResponse(departure), new StationResponse(arrival),
			sectionDto.getDistance());
	}

	public Long getId() {
		return id;
	}

	public StationResponse getDeparture() {
		return departure;
	}

	public StationResponse getArrival() {
		return arrival;
	}

	public int getDistance() {
		return distance;
	}
}
