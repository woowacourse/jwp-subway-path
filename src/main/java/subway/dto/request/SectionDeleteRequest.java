package subway.dto.request;

public class SectionDeleteRequest {
	private String upStationName;
	private String downStationName;

	public SectionDeleteRequest() {
	}

	public SectionDeleteRequest(final String upStationName, final String downStationName) {
		this.upStationName = upStationName;
		this.downStationName = downStationName;
	}

	public String getUpStationName() {
		return upStationName;
	}

	public String getDownStationName() {
		return downStationName;
	}
}
