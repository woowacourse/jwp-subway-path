package subway.ui.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class PathRequest {

	@NotBlank
	@Length(min = 1, max = 10, message = "{min}자 ~ {max}자 사이의 역의 이름만 입력해주세요.")
	private String departure;

	@NotBlank
	@Length(min = 1, max = 10, message = "{min}자 ~ {max}자 사이의 역의 이름만 입력해주세요.")
	private String arrival;

	public PathRequest() {
	}

	public PathRequest(final String departure, final String arrival) {
		this.departure = departure;
		this.arrival = arrival;
	}

	public String getDeparture() {
		return departure;
	}

	public String getArrival() {
		return arrival;
	}
}
