package subway.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

public class SectionRequest {

	@NotBlank
	@Length(min = 1, max = 10, message = "{min}자 ~ {max}자 사이의 역의 이름만 입력해주세요.")
	private String departure;

	@NotBlank
	@Length(min = 1, max = 10, message = "{min}자 ~ {max}자 사이의 역의 이름만 입력해주세요.")
	private String arrival;

	@NotNull
	@Positive(message = "거리는 양의 정수여야 합니다.")
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
