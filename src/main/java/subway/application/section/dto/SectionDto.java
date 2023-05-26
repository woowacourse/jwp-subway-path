package subway.application.section.dto;

import subway.domain.line.Distance;
import subway.domain.line.Section;
import subway.domain.line.Station;

public class SectionDto {

	private Long id;
	private String departure;
	private String arrival;
	private int distance;

	public SectionDto(final Long id, final String departure, final String arrival, final int distance) {
		this.id = id;
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
	}

	public static SectionDto from(final Section section) {
		final Station departure = section.getDeparture();
		final Station arrival = section.getArrival();
		final Distance distance = section.getDistance();
		return new SectionDto(section.getId(), departure.getName(), arrival.getName(), distance.getValue());
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
