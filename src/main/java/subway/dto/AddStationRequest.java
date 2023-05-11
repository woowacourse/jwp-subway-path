package subway.dto;

import javax.validation.constraints.PositiveOrZero;

public class AddStationRequest {


	private String departureStation;
	private String arrivalStation;

	@PositiveOrZero(message = "거리는 양의 정수여야 합니다.")
	private int distance;

	public AddStationRequest() {
	}

	public AddStationRequest(final String departureStation, final String arrivalStation, final int distance) {
		this.departureStation = departureStation;
		this.arrivalStation = arrivalStation;
		this.distance = distance;
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
