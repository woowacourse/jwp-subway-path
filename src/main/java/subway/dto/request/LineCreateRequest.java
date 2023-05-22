package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class LineCreateRequest {

	@NotBlank(message = "노선의 이름을 입력해주세요")
	private String name;

	private LineCreateRequest() {
	}

	public LineCreateRequest(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
