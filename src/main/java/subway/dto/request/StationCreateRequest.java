package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class StationCreateRequest {

	@NotBlank(message = "역 이름을 입력해주세요")
	private String name;

	public StationCreateRequest() {
	}

	public StationCreateRequest(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
