package subway.dao.section.dto;

public class SectionDto {

	private Long id;
	private String departure;
	private String arrival;
	private Integer distance;

	public SectionDto(final Long id, final String departure, final String arrival, final Integer distance) {
		this.id = id;
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
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

	public Integer getDistance() {
		return distance;
	}
}
