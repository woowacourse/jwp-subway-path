package subway.ui.dto;

import javax.validation.constraints.PositiveOrZero;

public class SectionRequest {

	private String departure;
	private String arrival;
	@PositiveOrZero(message = "거리는 양의 정수여야 합니다.")
	private int distance;

	public SectionRequest() {
	}

	public SectionRequest(final String departure, final String arrival, final int distance) {
		this.departure = departure;
		this.arrival = arrival;
		this.distance = distance;
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
