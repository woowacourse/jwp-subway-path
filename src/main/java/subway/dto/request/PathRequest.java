package subway.dto.request;

import javax.validation.constraints.NotBlank;

public class PathRequest {

	@NotBlank(message = "출발역을 입력해주세요")
	private String start;

	@NotBlank(message = "도착역을 입력해주세요")
	private String destination;

	public PathRequest() {
	}

	public PathRequest(final String start, final String destination) {
		this.start = start;
		this.destination = destination;
	}

	public String getStart() {
		return start;
	}

	public String getDestination() {
		return destination;
	}
}
