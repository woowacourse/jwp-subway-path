package subway.ui.station.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class StationRequest {

	@NotBlank
	@Length(min = 1, max = 10, message = "{min}자 ~ {max}자 사이의 역의 이름만 입력해주세요.")
	private String name;

	public StationRequest() {
	}

	public StationRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
