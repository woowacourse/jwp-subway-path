package subway.ui.dto.request;

public class StationRequest {
	private String name;

	public StationRequest() {
	}

	public StationRequest(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}