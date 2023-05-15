package subway.application;

public class SectionCreateRequest {
	private String upStationName;
	private String downStationName;

	private Long distance;

	public SectionCreateRequest() {
	}

	public SectionCreateRequest(final String upStationName, final String downStationName, final Long distance) {
		this.upStationName = upStationName;
		this.downStationName = downStationName;
		this.distance = distance;
	}

	public String getUpStationName() {
		return upStationName;
	}

	public String getDownStationName() {
		return downStationName;
	}

	public Long getDistance() {
		return distance;
	}
}
