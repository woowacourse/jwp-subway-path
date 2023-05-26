package subway.application.section.dto;

import subway.application.station.dto.StationDto;
import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.domain.line.Station;

public class SectionDto {

	private Long id;
	private StationDto departure;
	private StationDto arrival;
	private int distance;

	public SectionDto(final Long id, final StationDto departure, final StationDto arrival, final int distance) {
		this.id = id;
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
	}

	public static SectionDto from(final Section section) {
		final Station departure = section.getDeparture();
		final Station arrival = section.getArrival();
		final Distance distance = section.getDistance();
		return new SectionDto(section.getId(), new StationDto(departure), new StationDto(arrival), distance.getValue());
	}

	public Long getId() {
		return id;
	}

	public StationDto getDeparture() {
		return departure;
	}

	public StationDto getArrival() {
		return arrival;
	}

	public int getDistance() {
		return distance;
	}
}
