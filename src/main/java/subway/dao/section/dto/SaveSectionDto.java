package subway.dao.section.dto;

public class SaveSectionDto {

	private Long lineId;
	private int distance;
	private String departure;
	private String arrival;

	public SaveSectionDto(final Long lineId, final int distance, final String departure, final String arrival) {
		this.lineId = lineId;
		this.distance = distance;
		this.departure = departure;
		this.arrival = arrival;
	}

	public Long getLineId() {
		return lineId;
	}

	public int getDistance() {
		return distance;
	}

	public String getDeparture() {
		return departure;
	}

	public String getArrival() {
		return arrival;
	}
}
