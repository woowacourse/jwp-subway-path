package subway.dto.request;

public class StationUpdateRequest {
	private String name;

	public StationUpdateRequest() {
	}

	public StationUpdateRequest(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
