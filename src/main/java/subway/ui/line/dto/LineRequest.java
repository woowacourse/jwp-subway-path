package subway.ui.line.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class LineRequest {

	@NotBlank(message = "노선의 이름은 반드시 입력해주세요.")
	@Length(min = 1, max = 10, message = "{min}자 ~ {max}자 사이의 노선 이름만 입력해주세요.")
	private String name;

	@NotBlank(message = "노선의 색은 반드시 입력해주세요.")
	private String color;

	public LineRequest() {
	}

	public LineRequest(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

}
